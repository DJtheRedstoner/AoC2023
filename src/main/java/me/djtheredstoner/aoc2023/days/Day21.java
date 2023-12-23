package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Day21 implements DayBase {

    public void init(List<String> lines) {

    }

    record Pos(int x, int y) {
        Pos add(Pos p) { return new Pos(x + p.x, y + p.y); }
    }

    record State(Pos p, int dist) {}
    
    public void part1(List<String> lines) {
        Pos start = null;
        Set<Pos> rocks = new HashSet<>();
        int y = 0;
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') rocks.add(new Pos(x, y));
                if (line.charAt(x) == 'S') start = new Pos(x, y);
            }
            y++;
        }

        Set<State> visited = new HashSet<>();
        Deque<State> queue = new ArrayDeque<>();
        int atDist = 0;

        queue.add(new State(start, 0));

        while (!queue.isEmpty()) {
            State s = queue.pop();
            if (!visited.add(s)) continue;
            if (s.dist == 64) {
                atDist++;
                continue;
            }
            for (Pos offset : new Pos[]{new Pos(-1, 0), new Pos(0, 1),new Pos(1, 0), new Pos(0, -1)}) {
                Pos nP = s.p().add(offset);
                if (rocks.contains(nP)) continue;
                queue.add(new State(nP, s.dist + 1));
            }
        }

        System.out.println(atDist);
    }

    int atDist(Pos start, Set<Pos> rocks, int dist, int width, int height) {
        Set<State> visited = new HashSet<>();
        Deque<State> queue = new ArrayDeque<>();
        int atDist = 0;

        queue.add(new State(start, 0));

        while (!queue.isEmpty()) {
            State s = queue.pop();
            if (!visited.add(s)) continue;
            if (s.dist == dist) {
                atDist++;
                continue;
            }
            for (Pos offset : new Pos[]{new Pos(-1, 0), new Pos(0, 1),new Pos(1, 0), new Pos(0, -1)}) {
                Pos nP = s.p().add(offset);
                Pos rP = new Pos(((nP.x() % width) + width) % width, ((nP.y() % height) + height) % height);
                if (rocks.contains(rP)) continue;
                queue.add(new State(nP, s.dist + 1));
            }
        }

        return atDist;
    }

    public void part2(List<String> lines) {
        Pos start = null;
        Set<Pos> rocks = new HashSet<>();
        int y = 0;
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') rocks.add(new Pos(x, y));
                if (line.charAt(x) == 'S') start = new Pos(x, y);
            }
            y++;
        }

        int width = lines.get(0).length();
        int height = lines.size();

        System.out.println(height);

        System.out.println(atDist(start, rocks, 11, width, height));

        int a = atDist(start, rocks, 65, width, height);
        int b = atDist(start, rocks, 65 + height, width, height);
        int c = atDist(start, rocks, 65 + 2 * height, width, height);

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        int b0 = a;
        int b1 = b - a;
        int b2 = c - b;

        long n = 26501365 / height;

        System.out.println(b0 + b1 * n + (n*(n-1)/2)*(b2-b1));
    }

    public static void main(String...args) {
        new Day21().test(1);
        new Day21().test(2);
        new Day21().run();
    }
}
