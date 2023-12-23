package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Day22 implements DayBase {

    record Pos(int x, int y, int z) {
        Pos sub(int z) {
            return new Pos(x, y, z() - z);
        }

        static Pos fromString(String s) {
            String[] parts = s.split(",");
            return new Pos(
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2])
            );
        }
    }

    record Brick(UUID id, Pos start, Pos end) {

        Set<Pos> toCubes() {

            Set<Pos> cubes = new HashSet<>();
            for (int x = start.x(); x <= end.x(); x++) {
                for (int y = start.y(); y <= end.y(); y++) {
                    for (int z = start.z(); z <= end.z(); z++) {
                        cubes.add(new Pos(x, y, z));
                    }
                }
            }
            return cubes;
        }

    }

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        List<Brick> bricks = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("~");
            bricks.add(new Brick(UUID.randomUUID(), Pos.fromString(parts[0]), Pos.fromString(parts[1])));
        }

        Set<Pos> cubes = new HashSet<>();
        for (Brick brick : bricks) {
            cubes.addAll(brick.toCubes());
        }

        while (true) {
            boolean moved = false;
            outer:
            for (Brick brick : bricks) {
                cubes.removeAll(brick.toCubes());

                int minZ = brick.start.z();
                if (minZ == 1) {
                    cubes.addAll(brick.toCubes());
                    continue;
                }

                for (Pos cube : brick.toCubes()) {
                    if (cube.z() == minZ && cubes.contains(cube.sub(1))) {
                        cubes.addAll(brick.toCubes());
                        continue outer;
                    }
                }

                Brick newBrick = new Brick(brick.id(), brick.start.sub(1), brick.end.sub(1));
                bricks.set(bricks.indexOf(brick), newBrick);

                cubes.addAll(newBrick.toCubes());

                moved = true;
            }
            if (!moved) break;
        }

//        System.out.println(bricks);

        int count = 0;

        outer:
        for (Brick remove : bricks) {
            ArrayList<Brick> newBricks = new ArrayList<>(bricks);
            newBricks.remove(remove);
            if (!doesFall(newBricks)) {
                count++;
            }
        }

        System.out.println(count);
    }

    boolean doesFall(List<Brick> bricks) {
        boolean fell = false;
        for (Brick brick : bricks) {
            int minZ = Math.min(brick.start().z(), brick.end().z());
            if (minZ <= 1) continue;
            int newZ = minZ - 1;
            boolean blocked = false;
            for (Brick other : bricks) {
                if (other == brick) continue;
                if (other.start.z <= newZ && other.end.z >= newZ)
                    if (brick.start.x <= other.end.x && brick.end.x >= other.start.x &&
                        brick.start.y <= other.end.y && brick.end.y >= other.start.y) blocked = true;
            }
            if (!blocked) return true;
        }
        return false;
    }

    public void part2(List<String> lines) {
        List<Brick> bricks = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("~");
            bricks.add(new Brick(UUID.randomUUID(), Pos.fromString(parts[0]), Pos.fromString(parts[1])));
        }

        Set<Pos> cubes = new HashSet<>();
        for (Brick brick : bricks) {
            cubes.addAll(brick.toCubes());
        }

        while (true) {
            boolean moved = false;
            outer:
            for (Brick brick : bricks) {
                cubes.removeAll(brick.toCubes());

                int minZ = brick.start.z();
                if (minZ == 1) {
                    cubes.addAll(brick.toCubes());
                    continue;
                }

                for (Pos cube : brick.toCubes()) {
                    if (cube.z() == minZ && cubes.contains(cube.sub(1))) {
                        cubes.addAll(brick.toCubes());
                        continue outer;
                    }
                }

                Brick newBrick = new Brick(brick.id(), brick.start.sub(1), brick.end.sub(1));
                bricks.set(bricks.indexOf(brick), newBrick);

                cubes.addAll(newBrick.toCubes());

                moved = true;
            }
            if (!moved) break;
        }

        int count = 0;

        for (Brick remove : bricks) {
            ArrayList<Brick> newBricks = new ArrayList<>(bricks);
            newBricks.remove(remove);
            count += numFall(newBricks);
        }

        System.out.println(count);
    }

    int numFall(List<Brick> bricks) {
        Set<UUID> fallen = new HashSet<>();

        Set<Pos> cubes = new HashSet<>();
        for (Brick brick : bricks) {
            cubes.addAll(brick.toCubes());
        }

        while (true) {
            boolean moved = false;
            outer:
            for (Brick brick : bricks) {
                cubes.removeAll(brick.toCubes());

                int minZ = brick.start.z();
                if (minZ == 1) {
                    cubes.addAll(brick.toCubes());
                    continue;
                }

                for (Pos cube : brick.toCubes()) {
                    if (cube.z() == minZ && cubes.contains(cube.sub(1))) {
                        cubes.addAll(brick.toCubes());
                        continue outer;
                    }
                }

                fallen.add(brick.id());

                Brick newBrick = new Brick(brick.id(), brick.start.sub(1), brick.end.sub(1));
                bricks.set(bricks.indexOf(brick), newBrick);

                cubes.addAll(newBrick.toCubes());

                moved = true;
            }
            if (!moved) break;
        }


        return fallen.size();
    }

    public static void main(String...args) {
        new Day22().test(1);
        new Day22().test(2);
        new Day22().run();
    }
}
