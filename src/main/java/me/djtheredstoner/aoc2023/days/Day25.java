package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;
import org.jgrapht.Graph;
import org.jgrapht.alg.StoerWagnerMinimumCut;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day25 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        Set<Set<String>> connections = new HashSet<>();

        for (String line : lines) {
            String[] parts = line.split(": ");
            String left = parts[0];
            for (String s : parts[1].split(" ")) {
                connections.add(Set.of(left, s));
            }
        }

        Map<String, Set<String>> edges = new HashMap<>();

        for (Set<String> connection : connections) {
            var i = connection.iterator();
            var n1 = i.next();
            var n2 = i.next();
            edges.computeIfAbsent(n1, it -> new HashSet<>()).add(n2);
            edges.computeIfAbsent(n2, it -> new HashSet<>()).add(n1);
        }

        Graph<String, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);

        Set<String> added = new HashSet<>();

        for (Map.Entry<String, Set<String>> entry : edges.entrySet()) {
            if (added.add(entry.getKey())) {
                graph.addVertex(entry.getKey());
            }
            for (String s : entry.getValue()) {
                if (added.add(s)) {
                    graph.addVertex(s);
                }
                graph.addEdge(entry.getKey(), s);
            }
        }

        var cut = new StoerWagnerMinimumCut<>(graph);
        Set<String> left = cut.minCut();
        added.removeAll(left);

        System.out.println(left.size() * added.size());
    }

    public void part2(List<String> lines) {

    }

    public static void main(String...args) {
        new Day25().test(1);
        new Day25().test(2);
        new Day25().run();
    }
}
