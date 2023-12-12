package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Day12 implements DayBase {

    public void init(List<String> lines) {

    }

    boolean get(int[] springs, int i, int flags) {
        int val = springs[i];
        if (val == -1) return true;
        if (val == 0) return false;
        return (flags >> (springs[i] - 1) & 1) > 0;
    }

    public void part1(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            String[] s = line.split(" ");
            int[] springs = new int[s[0].length()];
            int flagIdx = 0;
            for (int i = 0; i < s[0].length(); i++) {
                switch (s[0].charAt(i)) {
                    case '#' -> springs[i] = -1;
                    case '?' -> springs[i] = 1 + flagIdx++;
                }
            }

            int[] groups = Arrays.stream(s[1].split(",")).mapToInt(Integer::parseInt).toArray();

            int count = 0;
            for (int i = 0; i < (1 << flagIdx); i++) {
                boolean valid = true;
                int groupId = 0;
                boolean inGroup = false;
                int groupSize = 0;
                for (int j = 0; j < springs.length; j++) {
                    if (get(springs, j, i)) {
                        if (inGroup) {
                            groupSize++;
                        } else {
                            inGroup = true;
                            groupSize = 1;
                        }
                    } else {
                        if (inGroup) {
                            inGroup = false;
                            if (groupId >= groups.length) {
                                valid = false;
                                break;
                            }
                            if (groupSize != groups[groupId]) {
                                valid = false;
                                break;
                            }
                            groupId++;
                        }
                    }
                }
                if (inGroup) {
                    inGroup = false;
                    if (groupId >= groups.length) {
                        valid = false;
                    } else if (groupSize != groups[groupId]) {
                        valid = false;
                    }
                    groupId++;
                }
                if (groupId != groups.length) valid = false;
                if (valid) count++;
            }
            sum += count;
        }
        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        long sum = 0;
        for (String line : lines) {
            String[] s = line.split(" ");
            String s0 = String.join("?", s[0], s[0], s[0], s[0], s[0]);
//            String s0 = s[0];
            int[] springs = new int[s0.length()];
            for (int i = 0; i < s0.length(); i++) {
                switch (s0.charAt(i)) {
                    case '#' -> springs[i] = -1;
                    case '?' -> springs[i] = 1;
                }
            }

            int[] groups = Arrays.stream(s[1].split(",")).mapToInt(Integer::parseInt).toArray();
            int[] newGroups = new int[groups.length * 5];
            for (int i = 0; i < 5; i++) {
                System.arraycopy(groups, 0, newGroups, i * groups.length, groups.length);
            }
            groups = newGroups;

            long i = apply(Arrays.stream(springs).boxed().toList(), Arrays.stream(groups).boxed().toList(), 0);
            sum += i;
        }
        System.out.println(sum);
    }

    record Context(List<Integer> springs, List<Integer> groups, int n) {}

    static HashMap<Context, Long> memoize = new HashMap<>();

    static long apply(List<Integer> springs, List<Integer> groups, int n) {
        Context c = new Context(springs, groups, n);
        if (memoize.containsKey(c)) return memoize.get(c);

        if (springs.isEmpty()) {
            if (groups.isEmpty()) {
                return 1;
            }
            else return 0;
        }

        int current = springs.getFirst();

        long res = switch (current) {
            case -1 -> applyBroken(springs, groups, n);
            case 0 -> applyWorking(springs, groups, n);
            case 1 -> applyWorking(springs, groups, n) + applyBroken(springs, groups, n);
            default -> throw new IllegalArgumentException();
        };

        memoize.put(c, res);
        return res;
    }

    static long applyWorking(List<Integer> springs, List<Integer> groups, int n) {
        if (n > 0) return 0;
        else return apply(springs.subList(1, Math.max(springs.size(), 1)), groups, 0);
    }

    static long applyBroken(List<Integer> springs, List<Integer> groups, int n) {
        if (groups.isEmpty()) return 0;
        int group = groups.getFirst();
        n++;
        if (group == n) {
            if (springs.size() < 2 || springs.get(1) > -1)
                return apply(springs.subList(Math.min(2, springs.size()), springs.size()), groups.subList(1, groups.size()), 0);
            else
                return 0;
        } else {
            if (n > group) {
                return 0;
            } else {
                return apply(springs.subList(1, Math.max(springs.size(), 1)), groups, n);
            }
        }
    }

    public static void main(String...args) {
        new Day12().test(1);
        new Day12().test(2);
        new Day12().run();
    }
}
