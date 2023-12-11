package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 implements DayBase {

    public void init(List<String> lines) {

    }

    record Star(long[] x, long[] y) {
        long dist(Star other) {
            return Math.abs(this.x()[0] - other.x()[0]) + Math.abs(this.y()[0] - other.y()[0]);
        }

        @Override
        public String toString() {
            return "Star(x=%d, y=%d)".formatted(x[0], y[0]);
        }
    }

    public void part1(List<String> lines) {
        int width = lines.get(0).length();
        int height = lines.size();

        boolean[] colMask = new boolean[width];
        boolean[] rowMask = new boolean[height];
        long[][] rows = new long[height][];
        long[][] cols = new long[width][];

        for (int i = 0; i < width; i++) {
            cols[i] = new long[]{i};
        }

        Set<Star> stars = new HashSet<>();

        {
            int y = 0;
            for (String line : lines) {
                long[] row = new long[]{y};
                rows[y] = row;
                int x = 0;
                for (char c : line.toCharArray()) {
                    if (c == '#') {
                        stars.add(new Star(cols[x], row));
                        colMask[x] = true;
                        rowMask[y] = true;
                    }
                    x++;
                }
                y++;
            }
        }

        int xOffset = 0;
        for (int i = 0; i < colMask.length; i++) {
            if (!colMask[i]) xOffset++;
            cols[i][0] += xOffset;
        }

        int yOffset = 0;
        for (int i = 0; i < rowMask.length; i++) {
            if (!rowMask[i]) yOffset++;
            rows[i][0] += yOffset;
        }

        long sum = 0;
        Set<Set<Star>> visited = new HashSet<>();
        for (Star star : stars) {
            for (Star star1 : stars) {
                if (star == star1) continue;
                var p = Set.of(star, star1);
                if (visited.contains(p)) continue;
                visited.add(p);
                sum += star.dist(star1);
            }
        }

        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        int width = lines.get(0).length();
        int height = lines.size();

        boolean[] colMask = new boolean[width];
        boolean[] rowMask = new boolean[height];
        long[][] rows = new long[height][];
        long[][] cols = new long[width][];

        for (int i = 0; i < width; i++) {
            cols[i] = new long[]{i};
        }

        Set<Star> stars = new HashSet<>();

        {
            int y = 0;
            for (String line : lines) {
                long[] row = new long[]{y};
                rows[y] = row;
                int x = 0;
                for (char c : line.toCharArray()) {
                    if (c == '#') {
                        stars.add(new Star(cols[x], row));
                        colMask[x] = true;
                        rowMask[y] = true;
                    }
                    x++;
                }
                y++;
            }
        }

        int xOffset = 0;
        for (int i = 0; i < colMask.length; i++) {
            if (!colMask[i]) xOffset += 1_000_000 - 1;
            cols[i][0] += xOffset;
        }

        int yOffset = 0;
        for (int i = 0; i < rowMask.length; i++) {
            if (!rowMask[i]) yOffset += 1_000_000 - 1;
            rows[i][0] += yOffset;
        }

        long sum = 0;
        Set<Set<Star>> visited = new HashSet<>();
        for (Star star : stars) {
            for (Star star1 : stars) {
                if (star == star1) continue;
                var p = Set.of(star, star1);
                if (visited.contains(p)) continue;
                visited.add(p);
                sum += star.dist(star1);
            }
        }

        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day11().test(1);
        new Day11().test(2);
        new Day11().run();
    }
}
