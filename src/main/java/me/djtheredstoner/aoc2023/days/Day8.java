package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 implements DayBase {

    public void init(List<String> lines) {

    }

    record Row(String name, String left, String right) {}

    public void part1(List<String> lines) {
        String instructions = lines.get(0);

        Pattern p = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

        Map<String, Row> rows = new HashMap<>();

        for (int i = 2; i < lines.size(); i++) {
            var m = p.matcher(lines.get(i));
            m.find();
            rows.put(m.group(1), new Row(m.group(1), m.group(2), m.group(3)));
        }

        int steps = 0;
        Row current = rows.get("AAA");
        do {
            if (instructions.charAt(steps % instructions.length()) == 'L') {
                current = rows.get(current.left());
            } else {
                current = rows.get(current.right());
            }
            steps++;
        } while (!current.name().equals("ZZZ"));

        System.out.println(steps);
    }

    int length(Map<String, Row> rows, Row start, String instructions) {
        int steps = 0;
        Row current = start;
        do {
            if (instructions.charAt(steps % instructions.length()) == 'L') {
                current = rows.get(current.left());
            } else {
                current = rows.get(current.right());
            }
            steps++;
        } while (!current.name().endsWith("Z"));

        return steps;
    }

    public void part2(List<String> lines) {
        String instructions = lines.get(0);

        Pattern p = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");

        Map<String, Row> rows = new HashMap<>();

        for (int i = 2; i < lines.size(); i++) {
            var m = p.matcher(lines.get(i));
            m.find();
            rows.put(m.group(1), new Row(m.group(1), m.group(2), m.group(3)));
        }

        long s =
            rows.values().stream()
                .filter(it -> it.name().endsWith("A"))
                .map(it -> length(rows, it, instructions))
                .map(it -> (long) it)
                    .reduce(1L, Day8::lcm);

        System.out.println(s);
    }

    public static long lcm(long number1, long number2) {
        BigInteger n1 = BigInteger.valueOf(number1);
        BigInteger n2 = BigInteger.valueOf(number2);
        BigInteger gcd = n1.gcd(n2);
        BigInteger absProduct = n1.multiply(n2).abs();
        return absProduct.divide(gcd).longValueExact();
    }

    public static void main(String...args) {
        new Day8().test(1);
        new Day8().test(2);
        new Day8().run();
    }
}
