package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day16 implements DayBase {

    public void init(List<String> lines) {

    }

    record Pos(int x, int y) {}

    record Dat(int x, int y, int vX, int vY) {}

    public void part1(List<String> lines) {
        Deque<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, 0, 1, 0});
        Set<Pos> visited = new HashSet<>();
        Set<Dat> visitedDat = new HashSet<>();
        while (!queue.isEmpty()) {

            int[] d = queue.removeFirst();
            int x = d[0];
            int y = d[1];
            int vX = d[2];
            int vY = d[3];
            if (x < 0 || x > lines.get(0).length() - 1 || y < 0 || y > lines.size() - 1) continue;
            visited.add(new Pos(x, y));
            Dat dat = new Dat(x, y, vX, vY);
            if (visitedDat.contains(dat)) continue;
            visitedDat.add(dat);
            switch (lines.get(y).charAt(x)) {
                case '.' -> queue.add(new int[]{x + vX, y + vY, vX, vY});
                case '/' -> {
                    if (vX == 1) queue.add(new int[]{x, y - 1, 0, -1});
                    if (vX == -1) queue.add(new int[]{x, y + 1, 0, 1});
                    if (vY == 1) queue.add(new int[]{x - 1, y, -1, 0});
                    if (vY == -1) queue.add(new int[]{x + 1, y, 1, 0});
                }
                case '\\' -> {
                    if (vX == 1) queue.add(new int[]{x, y + 1, 0, 1});
                    if (vX == -1) queue.add(new int[]{x, y - 1, 0, -1});
                    if (vY == 1) queue.add(new int[]{x + 1, y, 1, 0});
                    if (vY == -1) queue.add(new int[]{x - 1, y, -1, 0});
                }
                case '|' -> {
                    if (vX != 0) {
                        queue.add(new int[]{x, y + 1, 0, 1});
                        queue.add(new int[]{x, y - 1, 0, -1});
                    } else {
                        queue.add(new int[]{x + vX, y + vY, vX, vY});
                    }
                }
                case '-' -> {
                    if (vY != 0) {
                        queue.add(new int[]{x + 1, y, 1, 0});
                        queue.add(new int[]{x - 1, y, -1, 0});
                    } else {
                        queue.add(new int[]{x + vX, y + vY, vX, vY});
                    }
                }
                default -> throw new IllegalStateException(String.valueOf(lines.get(y).charAt(x)));
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(0).length(); j++) {
                if (visited.contains(new Pos(j, i))) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }

        System.out.println(visited.size());
    }

    int count(List<String> lines, int[] start) {
        Deque<int[]> queue = new LinkedList<>();
        queue.add(start);
        Set<Pos> visited = new HashSet<>();
        Set<Dat> visitedDat = new HashSet<>();
        while (!queue.isEmpty()) {

            int[] d = queue.removeFirst();
            int x = d[0];
            int y = d[1];
            int vX = d[2];
            int vY = d[3];
            if (x < 0 || x > lines.get(0).length() - 1 || y < 0 || y > lines.size() - 1) continue;
            visited.add(new Pos(x, y));
            Dat dat = new Dat(x, y, vX, vY);
            if (visitedDat.contains(dat)) continue;
            visitedDat.add(dat);
            switch (lines.get(y).charAt(x)) {
                case '.' -> queue.add(new int[]{x + vX, y + vY, vX, vY});
                case '/' -> {
                    if (vX == 1) queue.add(new int[]{x, y - 1, 0, -1});
                    if (vX == -1) queue.add(new int[]{x, y + 1, 0, 1});
                    if (vY == 1) queue.add(new int[]{x - 1, y, -1, 0});
                    if (vY == -1) queue.add(new int[]{x + 1, y, 1, 0});
                }
                case '\\' -> {
                    if (vX == 1) queue.add(new int[]{x, y + 1, 0, 1});
                    if (vX == -1) queue.add(new int[]{x, y - 1, 0, -1});
                    if (vY == 1) queue.add(new int[]{x + 1, y, 1, 0});
                    if (vY == -1) queue.add(new int[]{x - 1, y, -1, 0});
                }
                case '|' -> {
                    if (vX != 0) {
                        queue.add(new int[]{x, y + 1, 0, 1});
                        queue.add(new int[]{x, y - 1, 0, -1});
                    } else {
                        queue.add(new int[]{x + vX, y + vY, vX, vY});
                    }
                }
                case '-' -> {
                    if (vY != 0) {
                        queue.add(new int[]{x + 1, y, 1, 0});
                        queue.add(new int[]{x - 1, y, -1, 0});
                    } else {
                        queue.add(new int[]{x + vX, y + vY, vX, vY});
                    }
                }
                default -> throw new IllegalStateException(String.valueOf(lines.get(y).charAt(x)));
            }
        }

        return visited.size();
    }

    public void part2(List<String> lines) {
        int width = lines.get(0).length();
        int height = lines.size();

        Set<Integer> n = new HashSet<>();
        for (int i = 0; i < height; i++) {
            n.add(count(lines, new int[]{0, i, 1, 0}));
            n.add(count(lines, new int[]{width - 1, i, -1, 0}));
        }
        for (int i = 0; i < height; i++) {
            n.add(count(lines, new int[]{i, 0, 0, 1}));
            n.add(count(lines, new int[]{i, height - 1, 0, -1}));
        }

        System.out.println(n.stream().mapToInt(it -> it).max().getAsInt());
    }

    public static void main(String...args) {
        new Day16().test(1);
        new Day16().test(2);
        new Day16().run();
    }
}
