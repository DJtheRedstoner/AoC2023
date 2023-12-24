package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 implements DayBase {

    public void init(List<String> lines) {

    }

    record Pos(int x, int y) {
        List<Pos> neighbours() {
            return List.of(
                new Pos(x + 1, y),
                new Pos(x - 1, y),
                new Pos(x, y +1),
                new Pos(x, y - 1)
            );
        }
    }

    record Slope(Pos p, int dX, int dY) {}

    record State(Pos p, int dist, List<Pos> hist) {}

    public void part1(List<String> lines) {
        Set<Pos> paths = new HashSet<>();
        Map<Pos, Slope> slopes = new HashMap<>();

        int y = 0;
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '.' -> paths.add(new Pos(x, y));
                    case '>' -> slopes.put(new Pos(x, y), new Slope(new Pos(x, y), 1, 0));
                    case '<' -> slopes.put(new Pos(x, y), new Slope(new Pos(x, y), -1, 0));
                    case 'v' -> slopes.put(new Pos(x, y), new Slope(new Pos(x, y), 0, 1));
                    case '^' -> slopes.put(new Pos(x, y), new Slope(new Pos(x, y), 0, -1));
                }
            }
            y++;
        }

        Pos start = new Pos(1, 0);
        Pos end = new Pos(lines.get(0).length() - 2, lines.size() - 1);

        Deque<State> queue = new ArrayDeque<>();
//        Set<Pos> visited = new HashSet<>();
        queue.add(new State(start, 0, new ArrayList<>()));

        int maxLength = 0;

        while (!queue.isEmpty()) {
            State state = queue.removeFirst();
            Pos curr = state.p();

//            if (!visited.add(curr)) continue;

            if (curr.equals(end)) {
                if (state.dist > maxLength) maxLength = state.dist;
            }

            for (Pos p : curr.neighbours()) {
                if (state.hist.contains(p)) continue;

                boolean canSlide = false;
                if (slopes.containsKey(p)) {
                    Slope s = slopes.get(p);
                    if (s.dX == p.x - curr.x && s.dY == p.y - curr.y) canSlide = true;
                }
                if (paths.contains(p) || canSlide) {
                    ArrayList<Pos> hist = new ArrayList<>(state.hist);
                    hist.add(p);
                    queue.add(new State(p, state.dist + 1, hist));
                }
            }
        }

        System.out.println(maxLength);
    }

    public void part2(List<String> lines) {
        Set<Pos> paths = new HashSet<>();

        {
            int y = 0;
            for (String line : lines) {
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) != '#') paths.add(new Pos(x, y));
                }
                y++;
            }
        }

        Pos start = new Pos(1, 0);
        Pos end = new Pos(lines.get(0).length() - 2, lines.size() - 1);

        record Edge(Pos p, int dist) {}

        Map<Pos, Set<Edge>> neighbours = new HashMap<>();

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(0).length(); x++) {
                Pos p = new Pos(x, y);
                if (paths.contains(p)) {
                    neighbours.put(p, new HashSet<>(p.neighbours().stream().filter(paths::contains).map(it -> new Edge(it, 1)).collect(Collectors.toSet())));
                }
            }
        }

        while (true) {
            boolean modified = false;

            Map<Pos, Set<Edge>> newNeighbours = new HashMap<>(neighbours);

            for (Map.Entry<Pos, Set<Edge>> entry : neighbours.entrySet()) {
                if (entry.getValue().size() == 2) {
                    modified = true;
                    newNeighbours.remove(entry.getKey());
                    var i = entry.getValue().iterator();
                    var v1 = i.next();
                    var v2 = i.next();
                    newNeighbours.get(v1.p()).removeIf(it -> it.p().equals(entry.getKey()));
                    newNeighbours.get(v1.p()).add(new Edge(v2.p(), v2.dist + v1.dist));
                    newNeighbours.get(v2.p()).removeIf(it -> it.p().equals(entry.getKey()));
                    newNeighbours.get(v2.p()).add(new Edge(v1.p(), v1.dist + v2.dist));
                    break;
                }
            }

            neighbours = newNeighbours;

            if (!modified) break;
        }

        Deque<State> queue = new ArrayDeque<>();
        queue.add(new State(start, 0, new ArrayList<>()));

        int maxLength = 0;

        while (!queue.isEmpty()) {
            State state = queue.removeFirst();
            Pos curr = state.p();

            if (curr.equals(end)) {
                if (state.dist > maxLength) maxLength = state.dist;
            }

            Set<Edge> n = neighbours.get(curr);

            for (Edge p : neighbours.get(curr)) {
                if (state.hist.contains(p.p())) continue;

                ArrayList<Pos> hist = new ArrayList<>(state.hist);
                hist.add(p.p());

                int dist = state.dist + p.dist();

                queue.add(new State(p.p(), dist, hist));
            }
        }

        System.out.println(maxLength);
    }

    public static void main(String...args) {
        new Day23().test(1);
        new Day23().test(2);
        new Day23().run();
    }
}
