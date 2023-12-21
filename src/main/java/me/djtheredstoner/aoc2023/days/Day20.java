package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day20 implements DayBase {

    public void init(List<String> lines) {

    }

    int lowPulses = 0;
    int highPulses = 0;

    Set<String> pulsedHigh;

    record Pulse(String prev, String module, boolean high) {}

    void pulseCount(Pulse ip, Map<String, Module> modules, Map<String, Boolean> flipFlopStates, Map<String, Map<String, Boolean>> conjStates) {

        List<Pulse> queue = new ArrayList<>();
        queue.add(ip);

        while (!queue.isEmpty()) {
            Pulse p = queue.removeFirst();
            String module = p.module();
            boolean high = p.high();
            String prev = p.prev();

            Module m = modules.get(module);
            if (high) {
                highPulses++;

                if (pulsedHigh != null) {
                    pulsedHigh.add(prev);
                }
            } else {
                lowPulses++;
            }
            if (m == null) continue;
            if (m.type.startsWith("%")) {
                if (!high) {
                    boolean current = flipFlopStates.get(module);
                    flipFlopStates.put(module, !current);
                    for (String s : m.connections()) {
                        queue.add(new Pulse(module, s, !current));
                    }
                }
            } else if (m.type.startsWith("&")) {
                Map<String, Boolean> currentStates = conjStates.get(module);
                currentStates.put(prev, high);
                boolean allHigh = currentStates.values().stream().allMatch(it -> it);
                for (String s : m.connections()) {
                    queue.add(new Pulse(module, s, !allHigh));
                }
            } else if (m.type.equals("broadcaster")) {
                for (String s : m.connections()) {
                    queue.add(new Pulse(module, s, high));
                }
            } else {
                throw new IllegalArgumentException(module);
            }
        }
    }

    record Module(String type, List<String> connections) {}

    public void part1(List<String> lines) {
        Map<String, Module> modules = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(" -> ");
            String name = parts[0];
            String type;
            if (name.startsWith("%")) {
                type = "%";
                name = name.substring(1);
            } else if (name.startsWith("&")) {
                type = "&";
                name = name.substring(1);
            } else if (name.equals("broadcaster")) {
                type = "broadcaster";
            } else {
                throw new IllegalStateException(name);
            }
            modules.put(name, new Module(type, Arrays.asList(parts[1].split(", "))));
        }

        Map<String, Boolean> flipFlopStates = new HashMap<>();
        for (Map.Entry<String, Module> e : modules.entrySet()) {
            if (e.getValue().type.startsWith("%")) flipFlopStates.put(e.getKey(), false);
        }

        Map<String, Map<String, Boolean>> conjStates = new HashMap<>();
        for (Map.Entry<String, Module> e : modules.entrySet()) {
            if (e.getValue().type.startsWith("&")) {
                Map<String, Boolean> curr = new HashMap<>();
                for (Map.Entry<String, Module> entry : modules.entrySet()) {
                    if (entry.getValue().connections().contains(e.getKey())) {
                        curr.put(entry.getKey(), false);
                    }
                }
                conjStates.put(e.getKey(), curr);
            }
        }

        for (int i = 0; i < 1000; i++) {
            pulseCount(new Pulse("", "broadcaster", false), modules, flipFlopStates, conjStates);
        }

        System.out.println(lowPulses);
        System.out.println(highPulses);
        System.out.println(lowPulses * highPulses);
    }

    public void part2(List<String> lines) {
        Map<String, Module> modules = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(" -> ");
            String name = parts[0];
            String type;
            if (name.startsWith("%")) {
                type = "%";
                name = name.substring(1);
            } else if (name.startsWith("&")) {
                type = "&";
                name = name.substring(1);
            } else if (name.equals("broadcaster")) {
                type = "broadcaster";
            } else {
                throw new IllegalStateException(name);
            }
            modules.put(name, new Module(type, Arrays.asList(parts[1].split(", "))));
        }

        Map<String, Boolean> flipFlopStates = new HashMap<>();
        for (Map.Entry<String, Module> e : modules.entrySet()) {
            if (e.getValue().type.startsWith("%")) flipFlopStates.put(e.getKey(), false);
        }

        Map<String, Map<String, Boolean>> conjStates = new HashMap<>();
        for (Map.Entry<String, Module> e : modules.entrySet()) {
            if (e.getValue().type.startsWith("&")) {
                Map<String, Boolean> curr = new HashMap<>();
                for (Map.Entry<String, Module> entry : modules.entrySet()) {
                    if (entry.getValue().connections().contains(e.getKey())) {
                        curr.put(entry.getKey(), false);
                    }
                }
                conjStates.put(e.getKey(), curr);
            }
        }


        Map<String, List<Integer>> observations = new HashMap<>();
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            if (entry.getValue().type.equals("&")) {
                observations.put(entry.getKey(), new ArrayList<>());
            }
        }

        for (int i = 0; i < 10000; i++) {
            pulsedHigh = new HashSet<>();
            pulseCount(new Pulse("", "broadcaster", false), modules, flipFlopStates, conjStates);
            for (String s : observations.keySet()) {
                if (pulsedHigh.contains(s)) observations.get(s).add(i);
            }
        }

        List<Integer> factors = new ArrayList<>();
        for (List<Integer> value : observations.values()) {
            factors.add(value.get(1) - value.get(0));
        }

        System.out.println(factors.stream().mapToLong(it -> it).reduce(1, Day8::lcm));
    }

    public static void main(String...args) {
        new Day20().test(1);
        new Day20().test(2);
        new Day20().run();
    }
}
