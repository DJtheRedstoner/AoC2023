package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day5 implements DayBase {

    public void init(List<String> lines) {

    }

    record Range(long in, long out, long length) {
        boolean inRange(long i) {
            return i >= in && i <= in + length;
        }
    }

    private List<Long> numbers(String s) {
        List<Long> numbers = new ArrayList<>();
        for (String n : s.split(" ")) {
            numbers.add(Long.parseLong(n));
        }
        return numbers;
    }

    private List<Range> convert(String input) {
        String[] lines = input.split("\n");

        List<Range> ranges = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            var n = numbers(lines[i]);

            ranges.add(new Range(n.get(1), n.get(0), n.get(2)));
        }

        return ranges;
    }

    private long get(List<Range> ranges, long key) {
        for (Range range : ranges) {
            if (range.inRange(key)) {
                return (key - range.in() + range.out());
            }
        }
        return key;
    }

    public void part1(List<String> lines) {
        String[] sections = String.join("\n", lines).split("\n\n");
        List<Long> seeds = numbers(sections[0].split(": ")[1]);

        var a = convert(sections[1]);
        var b = convert(sections[2]);
        var c = convert(sections[3]);
        var d = convert(sections[4]);
        var e = convert(sections[5]);
        var f = convert(sections[6]);
        var g = convert(sections[7]);

        System.out.println(seeds.stream().mapToLong(it -> get(g, get(f, get(e, get(d, get(c, get(b, get(a, it)))))))).min().getAsLong());
    }

    public void part2(List<String> lines) {
        String[] sections = String.join("\n", lines).split("\n\n");
        List<Long> seeds = numbers(sections[0].split(": ")[1]);

        var a = convert(sections[1]);
        var b = convert(sections[2]);
        var c = convert(sections[3]);
        var d = convert(sections[4]);
        var e = convert(sections[5]);
        var f = convert(sections[6]);
        var g = convert(sections[7]);

        long minLoc = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size(); i += 2) {
            for (long j = seeds.get(i); j <= seeds.get(i) + seeds.get(i + 1); j++) {
                long loc = get(g, get(f, get(e, get(d, get(c, get(b, get(a, j)))))));
                if (loc < minLoc) minLoc = loc;
            }
        }

        System.out.println(minLoc);
    }

    public static void main(String...args) {
        new Day5().test(1);
        new Day5().test(2);
        new Day5().run();
    }
}
