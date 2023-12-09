package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class Day9 implements DayBase {

    public void init(List<String> lines) {

    }

    int[] differences(int[] data) {
        int[] diff = new int[data.length - 1];
        for (int i = 0; i < data.length - 1; i++) {
            diff[i] = data[i + 1] - data[i];
        }
        return diff;
    }

    boolean isZero(int[] data) {
        for (int datum : data) {
            if (datum != 0) return false;
        }
        return true;
    }

    int last(int[] d) {
        return d[d.length - 1];
    }

    public void part1(List<String> lines) {
        int sum = 0;

        for (String line : lines) {
            int[] ns = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            Deque<int[]> differences = new ArrayDeque<>();
            differences.add(ns);
            differences.add(differences(ns));
            while (!isZero(differences.peekLast())) {
                differences.add(differences(differences.peekLast()));
            }

            sum += differences.stream().mapToInt(this::last).sum();
        }
        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        int sum = 0;

        for (String line : lines) {
            int[] ns = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            Deque<int[]> differences = new ArrayDeque<>();
            differences.add(differences(ns));
            while (!isZero(differences.peekLast())) {
                differences.add(differences(differences.peekLast()));
            }

            int n = differences.removeLast()[0];
            while (!differences.isEmpty()) {
                n = differences.removeLast()[0] - n;
            }
            System.out.println(ns[0] - n);
            sum += ns[0] - n;
        }
        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day9().test(1);
        new Day9().test(2);
        new Day9().run();
    }
}
