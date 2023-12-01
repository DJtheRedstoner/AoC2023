package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 implements DayBase {

    public void init(List<String> lines) {

    }

    public void part1(List<String> lines) {
        int n = 0;
        for (String line : lines) {
            String l = line.replaceAll("\\D", "");
            n += Integer.parseInt("" + l.charAt(0) + l.charAt(l.length() - 1));
        }
        System.out.println(n);
    }

    public void part2(List<String> lines) {
        String[] numbers = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        int n = 0;
        Pattern p = Pattern.compile("(\\d)");
        for (String line : lines) {

            Map<Integer, Integer> locations = new HashMap<>();

            Matcher m = p.matcher(line);
            while (m.find()) {
                locations.put(m.start(), Integer.parseInt(m.group()));
            }

            for (int i = 0; i < numbers.length; i++) {
                if (line.indexOf(numbers[i]) >= 0)
                    locations.put(line.indexOf(numbers[i]), i + 1);
                if (line.lastIndexOf(numbers[i]) >= 0)
                    locations.put(line.lastIndexOf(numbers[i]), i + 1);
            }

            n += locations.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getKey)).get().getValue() * 10 + locations.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getKey)).get().getValue();
        }
        System.out.println(n);
    }

    public static void main(String...args) {
        new Day1().run();
    }
}
