package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day10 implements DayBase {

    record Pos(int x, int y) {
        Pos add(int x, int y) {
            return new Pos(this.x + x, this.y + y);
        }
    }

    record Entry(Pos p, int dist) {}

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        var pipes = new HashMap<Pos, String>();
        int y = 0;
        Pos start = null;
        for (String line : lines) {
            int x = 0;
            for (char c : line.toCharArray()) {
                if (c != '.') {
                    var p = new Pos(x, y);
                    pipes.put(p, "" + c);
                    if (c == 'S') {
                        start = p;
                    }
                }
                x++;
            }
            y++;
        }
        
        Set<Pos> visited = new HashSet<>();
        Deque<Entry> q = new ArrayDeque<>();
        Map<Pos, Integer> dists = new HashMap<>();
        q.add(new Entry(start, 0));
        while (!q.isEmpty()) {
            Entry e = q.pop();
            Pos p = e.p();
            int d = e.dist() + 1;

            if (pipes.get(p) == null) continue;

            Integer prevDist = dists.get(p);
            if (prevDist == null || e.dist() < prevDist) {
                dists.put(p, e.dist());
            } else {
                continue;
            }

            dists.put(p, e.dist());
            visited.add(p);
            switch (pipes.get(p)) {
                case "|" -> {
                    q.add(new Entry(p.add(0, -1), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                case "-" -> {
                    q.add(new Entry(p.add(-1, 0), d));
                    q.add(new Entry(p.add(1, 0), d));
                }
                case "L" -> {
                    q.add(new Entry(p.add(0, -1), d));
                    q.add(new Entry(p.add(1, 0), d));
                }
                case "J" -> {
                    q.add(new Entry(p.add(0, -1), d));
                    q.add(new Entry(p.add(-1, 0), d));
                }
                case "7" -> {
                    q.add(new Entry(p.add(-1, 0), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                case "F" -> {
                    q.add(new Entry(p.add(1, 0), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                case "S" -> {
                    q.add(new Entry(p.add(-1, 0), d));
//                    q.add(new Entry(p.add(1, 0), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                default -> throw new IllegalStateException("Unexpected value: " + pipes.get(p));
            }
        }

        System.out.println(dists.values().stream().mapToInt(it -> it).max().getAsInt());
    }

    public void part2(List<String> lines) {
        var pipes = new HashMap<Pos, String>();
        Pos start = null;
        {
            int y = 0;

            for (String line : lines) {
                int x = 0;
                for (char c : line.toCharArray()) {
                    if (c != '.') {
                        var p = new Pos(x, y);
                        pipes.put(p, "" + c);
                        if (c == 'S') {
                            start = p;
                        }
                    }
                    x++;
                }
                y++;
            }
        }

        Set<Pos> visited = new HashSet<>();
        Deque<Entry> q = new ArrayDeque<>();
        Map<Pos, Integer> dists = new HashMap<>();
        q.add(new Entry(start, 0));
        while (!q.isEmpty()) {
            Entry e = q.pop();
            Pos p = e.p();
            int d = e.dist() + 1;

            if (pipes.get(p) == null) continue;

            Integer prevDist = dists.get(p);
            if (prevDist == null || e.dist() < prevDist) {
                dists.put(p, e.dist());
            } else {
                continue;
            }

            dists.put(p, e.dist());
            visited.add(p);
            switch (pipes.get(p)) {
                case "|" -> {
                    q.add(new Entry(p.add(0, -1), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                case "-" -> {
                    q.add(new Entry(p.add(-1, 0), d));
                    q.add(new Entry(p.add(1, 0), d));
                }
                case "L" -> {
                    q.add(new Entry(p.add(0, -1), d));
                    q.add(new Entry(p.add(1, 0), d));
                }
                case "J" -> {
                    q.add(new Entry(p.add(0, -1), d));
                    q.add(new Entry(p.add(-1, 0), d));
                }
                case "7" -> {
                    q.add(new Entry(p.add(-1, 0), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                case "F" -> {
                    q.add(new Entry(p.add(1, 0), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                case "S" -> {
                    q.add(new Entry(p.add(-1, 0), d));
                    //                    q.add(new Entry(p.add(1, 0), d));
                    q.add(new Entry(p.add(0, 1), d));
                }
                default -> throw new IllegalStateException("Unexpected value: " + pipes.get(p));
            }
        }

        List<String> newLines = new ArrayList<>();
        for (int y = 0; y < lines.size(); y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < lines.get(0).length(); x++) {
                if (visited.contains(new Pos(x, y))) {
                    sb.append(pipes.get(new Pos(x, y)));
                } else {
                    sb.append('.');
                }
            }
            newLines.add(sb.toString());
        }

        int count = 0;

        for (String newLine : newLines) {
            newLine = newLine.replaceAll("L-*J", "").replaceAll("F-*7", "").replaceAll("F-*J", "|").replaceAll("L-*7", "|");
            System.out.println(newLine);
            for (int i = 0; i < newLine.length(); i++) {
                if (newLine.charAt(i) == '.') {
                    if (count(newLine.substring(i), '|') % 2 == 1) {
                        count++;
                    }
                }
            }
        }

        System.out.println(count);
    }

    int count(String s, char c) {
        int count = 0;
        for (char c1 : s.toCharArray()) {
            if (c1 == c) count++;
        }
        return count;
    }

    public static void main(String...args) {
        new Day10().test(1);
        new Day10().test(2);
        new Day10().run();
    }
}
