package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day18 implements DayBase {

    public void init(List<String> lines) {

    }

    record Pos(int x, int y) {
        Pos add(Pos delta) { return new Pos (x + delta.x(), y + delta.y()); }
        Pos times(int scalar) { return new Pos(x * scalar, y * scalar); }
    }

    public void part1(List<String> lines) {
        Set<Pos> edges = new HashSet<>();

        Pos current = new Pos(0, 0);
        edges.add(current);

        for (String line : lines) {
            String[] d = line.split(" ");
            String direction = d[0];
            int dist = Integer.parseInt(d[1]);
            Pos delta = switch (direction) {
                case "U" -> new Pos(0, -1);
                case "D" -> new Pos(0, 1);
                case "L" -> new Pos(-1, 0);
                case "R" -> new Pos(1, 0);
                default -> throw new IllegalStateException(direction);
            };
            for (int i = 0; i < dist; i++) {
                current = current.add(delta);
                edges.add(current);
            }
        }

//        for (int i = -100; i < 100; i++) {
//            for (int j = -100; j < 100; j++) {
//                if (edges.contains(new Pos(j, i))) {
//                    System.out.print('#');
//                } else {
//                    System.out.print('.');
//                }
//            }
//            System.out.println();
//        }

        int minY = edges.stream().mapToInt(Pos::y).min().getAsInt();
        Pos startEdge = edges.stream().filter(it -> it.y() == minY && !edges.contains(new Pos(it.x(), minY + 1))).findFirst().get();
        Pos start = startEdge.add(new Pos(0, 1));

        System.out.println( start);

        Set<Pos> all = new HashSet<>(edges);

        Deque<Pos> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Pos p = queue.removeFirst();
            if (!all.add(p)) continue;
            for (Pos offset : new Pos[]{new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1), new Pos(0, 1), new Pos(1, 1), new Pos(1, 0), new Pos(1, -1), new Pos(0, -1)}) {
                Pos nP = p.add(offset);
                if (!all.contains(nP)) queue.add(nP);
            }
        }

        System.out.println(all.size());
    }

    public void part2(List<String> lines) {
        Set<Pos> edges = new HashSet<>();

        long length = 0;
        long area = 0;

        Pos prev = null;
        Pos current = new Pos(0, 0);

        for (String line : lines) {
            String[] d = line.split(" ");

            // (#7399f3)
            int dist = Integer.parseInt(d[2].substring(2, 7), 16);

            String direction = String.valueOf(d[2].charAt(7));
            Pos delta = switch (direction) {
                case "3" -> new Pos(0, -1);
                case "1" -> new Pos(0, 1);
                case "2" -> new Pos(-1, 0);
                case "0" -> new Pos(1, 0);
                default -> throw new IllegalStateException(direction);
            };

            length += dist;

            if (prev != null) {
                current = current.add(delta.times(dist));
                area += (((long) current.y + prev.y) * ((long) prev.x - current.x)) / 2;
            }

            prev = current;
        }
        System.out.println(area + length/2 + 1);
    }

    public static void main(String...args) {
        new Day18().test(1);
        new Day18().test(2);
        new Day18().run();
    }
}
