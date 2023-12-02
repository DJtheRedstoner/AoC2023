package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        Pattern p = Pattern.compile("Game (\\d+): (.+)");

        int sum = 0;

        for (String line : lines) {
            Matcher m = p.matcher(line);
            m.find();
            int id = Integer.parseInt(m.group(1));
            String rest = m.group(2);
            String[] subsets = rest.split("; ");
            boolean valid = true;
            for (String subset : subsets) {
                for (String cube : subset.split(", ")) {
                    String[] dat = cube.split(" ");
                    int n = Integer.parseInt(dat[0]);
                    switch (dat[1]) {
                        case "red" -> {
                            if (n > 12) valid = false;
                        }
                        case "green" -> {
                            if (n > 13) valid = false;
                        }
                        case "blue" -> {
                            if (n > 14) valid = false;
                        }
                    }
                }
            }
            if (valid) {
                sum += id;
            }
        }

        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        Pattern p = Pattern.compile("Game (\\d+): (.+)");

        int sum = 0;

        for (String line : lines) {
            Matcher m = p.matcher(line);
            m.find();
            int id = Integer.parseInt(m.group(1));
            String rest = m.group(2);
            String[] subsets = rest.split("; ");

            Map<String, Integer> cubes = new HashMap<>();

            for (String subset : subsets) {
                for (String cube : subset.split(", ")) {
                    String[] dat = cube.split(" ");
                    int n = Integer.parseInt(dat[0]);

                    if (!cubes.containsKey(dat[1])) {
                        cubes.put(dat[1], n);
                    } else {
                        if (n > cubes.get(dat[1])) {
                            cubes.put(dat[1], n);
                        }
                    }

                }
            }
            sum += cubes.values().stream().reduce(1, (a, b) -> a * b);
        }

        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day2().test(1);
        new Day2().test(2);
        new Day2().run();
    }
}
