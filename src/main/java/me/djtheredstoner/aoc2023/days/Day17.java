package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day17 implements DayBase {

    public void init(List<String> lines) {

    }

    record Pos(int x, int y) {
        Pos add(int dX, int dY) {
            return new Pos(x + dX, y + dX);
        }
    }

    record State(int x, int y, int inDir, int dir) {}

    public void part1(List<String> lines) {
        int[][] grid = new int[lines.size()][lines.get(0).length()];

        int width = grid[0].length;
        int height = grid.length;

        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
            for (int j = 0; j < l.length(); j++) {
                grid[i][j] = Character.digit(l.charAt(j), 10);
            }
        }

        Map<State, Integer> dists = new HashMap<>();
        Map<State, State> parents = new HashMap<>();
        Queue<State> queue = new PriorityQueue<>(Comparator.comparingInt(dists::get));

        var home = new State(0, 0, 0, 0);
        queue.add(home);
        dists.put(home, 0);
        parents.put(home, null);

        State finalState = null;

        while (!queue.isEmpty()) {
            State s = queue.remove();

            if (s.x == width - 1 && s.y == height -1) {
                finalState = s;
                break;
            }

            for(int[] delta : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1},}) {
                int nX = s.x() + delta[0];
                int nY = s.y() + delta[1];
                if (nX < 0 || nX > width - 1 || nY < 0 || nY > height - 1) continue;
                if (parents.get(s) != null) {
                    State p = parents.get(s);
                    if (p.x() == nX && p.y() == nY) continue;
                }

                int inDir;
                int dir = delta[0] + delta[1] * 2;

                if (s.dir == dir) {
                    if (s.inDir >= 3) continue;
                    inDir = s.inDir + 1;
                } else {
                    inDir = 1;
                }

                var nS = new State(nX, nY, inDir, dir);

                int dist = dists.get(s) + grid[nY][nX];

                if (dists.containsKey(nS)) {
                    if (dists.get(nS) <= dist) continue;
                }

                dists.put(nS, dist);
                parents.put(nS, s);
                queue.add(nS);
            }
        }

//        State s = finalState;
//        while (s != null) {
//            System.out.println(s);
//            s = parents.get(s);
//        }

        System.out.println(dists.get(finalState));
    }

    public void part2(List<String> lines) {
        int[][] grid = new int[lines.size()][lines.get(0).length()];

        int width = grid[0].length;
        int height = grid.length;

        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
            for (int j = 0; j < l.length(); j++) {
                grid[i][j] = Character.digit(l.charAt(j), 10);
            }
        }

        Map<State, Integer> dists = new HashMap<>();
        Map<State, State> parents = new HashMap<>();
        Queue<State> queue = new PriorityQueue<>(Comparator.comparingInt(dists::get));

        for (State start : new State[]{new State(0, 0, 0, 1), new State(0, 0, 0, 2)}) {
            dists.put(start, 0);
            parents.put(start, null);
            queue.add(start);
        }

        State finalState = null;

        while (!queue.isEmpty()) {
            State s = queue.remove();

            if (s.x == width - 1 && s.y == height -1 && s.inDir > 3) {
                finalState = s;
                break;
            }

            for(int[] delta : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1},}) {
                int nX = s.x() + delta[0];
                int nY = s.y() + delta[1];
                if (nX < 0 || nX > width - 1 || nY < 0 || nY > height - 1) continue;
                if (parents.get(s) != null) {
                    State p = parents.get(s);
                    if (p.x() == nX && p.y() == nY) continue;
                }

                int inDir;
                int dir = delta[0] + delta[1] * 2;

                if (s.dir == dir) {
                    if (s.inDir >= 10) continue;
                    inDir = s.inDir + 1;
                } else {
                    if (s.inDir < 4) continue;
                    inDir = 1;
                }

                var nS = new State(nX, nY, inDir, dir);

                int dist = dists.get(s) + grid[nY][nX];

                if (dists.containsKey(nS)) {
                    if (dists.get(nS) <= dist) continue;
                }

                dists.put(nS, dist);
                parents.put(nS, s);
                queue.add(nS);
            }
        }

//        State s = finalState;
//        while (s != null) {
//            System.out.println(s);
//            s = parents.get(s);
//        }

        System.out.println(dists.get(finalState));
    }

    public static void main(String...args) {
        new Day17().test(1);
        new Day17().test(2);
        new Day17().run();
    }
}
