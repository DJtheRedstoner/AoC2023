package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 implements DayBase {

    public void init(List<String> lines) {

    }

    record Pos(int x, int y) {
        Pos add(Pos p) {
            return new Pos(this.x + p.x, this.y + p.y);
        }
    }

    boolean containsSymbol(String t, int start, int end) {
        start = Math.max(start, 0);
        end = Math.min(end, t.length() - 1);
        for (char c : t.substring(start, end + 1).toCharArray()) {
            if (!Character.isDigit(c) && c != '.') {
                return true;
            }
        }
        return false;
    }

    public void part1(List<String> lines) {
        Map<Pos, String> data = new HashMap<>();
        int y = 0;
        Pattern p = Pattern.compile("(\\d+)");

        int sum = 0;

        for (int i = 0; i < lines.size(); i++) {
            Matcher m = p.matcher(lines.get(i));

            while (m.find()) {
                boolean valid = containsSymbol(lines.get(i), m.start() - 1, m.end());
                if (i > 0) {
                    if (containsSymbol(lines.get(i - 1), m.start() - 1, m.end())) valid = true;
                }
                if (i < lines.size() - 1) {
                    if (containsSymbol(lines.get(i + 1), m.start() - 1, m.end())) valid = true;
                }
                if (valid) {
                    sum += Integer.parseInt(m.group(1));
                }
            }
        }

        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        Map<UUID, Integer> trueNumber = new HashMap<>();
        Map<Pos, UUID> numbers = new HashMap<>();
        Set<Pos> gears = new HashSet<>();

        Pattern p = Pattern.compile("(\\d+)");

        int y = 0;
        for (String line : lines) {
            Matcher m = p.matcher(line);
            while (m.find()) {
                int n = Integer.parseInt(m.group(1));
                UUID u = UUID.randomUUID();
                trueNumber.put(u, n);
                for (int x = m.start(); x < m.end(); x++) {
                    numbers.put(new Pos(x, y), u);
                }
            }

            for (int i = 0; i < line.toCharArray().length; i++) {
                if (line.charAt(i) == '*') {
                    gears.add(new Pos(i, y));
                }
            }

            y++;
        }

        int sum = 0;

        for (Pos gear : gears) {
            Set<UUID> nums = new HashSet<>();
            for (Pos offset : new Pos[]{new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1), new Pos(0, 1), new Pos(1, 1), new Pos(1, 0), new Pos(1, -1), new Pos(0, -1)}) {
                if (numbers.get(gear.add(offset)) != null) {
                    nums.add(numbers.get(gear.add(offset)));
                }
            }
            if (nums.size() == 2) {
                sum += nums.stream().map(trueNumber::get).reduce(1, (a, b) -> a * b);
            }
        }

        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day3().test(1);
        new Day3().test(2);
        new Day3().run();
    }
}
