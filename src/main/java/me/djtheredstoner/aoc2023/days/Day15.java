package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        int sum = 0;
        for (String chunk : String.join("", lines).split(",")) {
            sum += HASH(chunk);
        }
        System.out.println(sum);
    }

    private int HASH(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash += s.charAt(i);
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

    record Lens(String label, int power) {}

    private int remove(ArrayList<Lens> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).label().equals(id)) {
                list.remove(i);
                return i;
            }
        }
        return -1;
    }

    public void part2(List<String> lines) {
        Map<Integer, ArrayList<Lens>> boxes = new HashMap<>();

        for (String chunk : String.join("", lines).split(",")) {
            String label = chunk.split("[-=]")[0];
            int boxId = HASH(label);
            ArrayList<Lens> box = boxes.computeIfAbsent(boxId, _ -> new ArrayList<>());
            if (chunk.contains("-")) {
                remove(box, label);
            } else if (chunk.contains("=")) {
                int power = Integer.parseInt(chunk.split("=")[1]);
                Lens lens = new Lens(label, power);
                if (box.stream().anyMatch(it -> it.label().equals(label))) {
                    int i = remove(box, label);
                    box.add(i, lens);
                } else {
                    box.add(lens);
                }
            }
        }

        System.out.println(boxes);

        int sum = 0;
        for (Map.Entry<Integer, ArrayList<Lens>> entry : boxes.entrySet()) {
            int box = entry.getKey() + 1;
            for (int i = 0; i < entry.getValue().size(); i++) {
                sum += box * (i + 1) * entry.getValue().get(i).power();
            }
        }
        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day15().test(1);
        new Day15().test(2);
        new Day15().run();
    }
}
