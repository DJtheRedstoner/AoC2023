package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.List;

public class Day6 implements DayBase {

    List<Race> races = new ArrayList<>();

    private List<Integer> numbers(String s) {
        List<Integer> numbers = new ArrayList<>();
        for (String n : s.split(" ")) {
            if (!n.isEmpty())
                numbers.add(Integer.parseInt(n));
        }
        return numbers;
    }

    record Race(int time, int record) {

    }

    public void init(List<String> lines) {
        var times = numbers(lines.get(0).split(": ")[1]);
        var dists = numbers(lines.get(1).split(": ")[1]);
        for (int i = 0; i < times.size(); i++) {
            races.add(new Race(times.get(i), dists.get(i)));
        }
    }

    public void part1(List<String> lines) {
        int acc = 1;

        for (Race race : races) {
            int wins = 0;
            for (int i = 0; i < race.time(); i++) {
                if ((race.time() - i) * i > race.record()) {
                    wins++;
                }
            }
            acc *= wins;
        }

        System.out.println(acc);
    }

    public void part2(List<String> lines) {
        var time = Long.parseLong(lines.get(0).split(": ")[1].replace(" ", ""));
        var record = Long.parseLong(lines.get(1).split(": ")[1].replace(" ", ""));

        int wins = 0;
        for (int i = 0; i < time; i++) {
            if ((time - i) * i > record) {
                wins++;
            }
        }

        System.out.println(wins);
    }

    public static void main(String...args) {
        new Day6().test(1);
        new Day6().test(2);
        new Day6().run();
    }
}
