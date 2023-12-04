package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class Day4 implements DayBase {

    public void init(List<String> lines) {

    }

    Set<Integer> parseInts(String s) {
        HashSet<Integer> set = new HashSet<>();
        for (String string : s.split(" ")) {
            if (!string.trim().isEmpty())
                set.add(Integer.parseInt(string.trim()));
        }
        return set;
    }

    public void part1(List<String> lines) {
        Pattern p = Pattern.compile("Card (\\d):");
        int sum = 0;

        for (String line : lines) {
            String[] parts = line.split(":")[1].split("\\|");
            Set<Integer> winning = parseInts(parts[0]);
            Set<Integer> myNums = parseInts(parts[1]);

            int score = 0;

            for (Integer myNum : myNums) {
                if (winning.contains(myNum)) {
                    if (score == 0) {
                        score = 1;
                    } else {
                        score *= 2;
                    }
                }
            }
            sum += score;
        }

        System.out.println(sum);
    }

    public void processLine(String line, List<String> lines, AtomicInteger sum) {
        sum.addAndGet(1);

        Pattern p = Pattern.compile("Card +(\\d+):");
        var m = p.matcher(line); m.find();
        int id = Integer.parseInt(m.group(1));

        String[] parts = line.split(":")[1].split("\\|");
        Set<Integer> winning = parseInts(parts[0]);
        Set<Integer> myNums = parseInts(parts[1]);

        int score = 0;

        for (Integer myNum : myNums) {
            if (winning.contains(myNum)) {
                score++;
            }
        }

        for (int i = 0; i < score; i++) {
            processLine(lines.get(id + 1 - 1 + i), lines, sum);
        }
    }

    public void part2(List<String> lines) {
        Pattern p = Pattern.compile("Card (\\d):");
        var i = new AtomicInteger(0);

        for (String line : lines) {
            processLine(line, lines, i);
        }

        System.out.println(i.get());
    }

    public static void main(String...args) {
        new Day4().test(1);
        new Day4().test(2);
        new Day4().run();
    }
}
