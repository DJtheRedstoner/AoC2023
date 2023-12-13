package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Day13 implements DayBase {

    public void init(List<String> lines) {

    }

    boolean isMirror(List<BitSet> bits, int index) {
        List<BitSet> left = bits.subList(0, index).reversed();
        List<BitSet> right = bits.subList(index, bits.size());
        int i = 0;
        while (i < left.size() && i < right.size()) {
            if (left.get(i).equals(right.get(i++))) continue;
            return false;
        }
        return true;
    }

    int findMirror(List<BitSet> bits, int ignore) {
        for (int i = 1; i < bits.size(); i++) {
            if (isMirror(bits, i) && i != ignore) return i;
        }
        return -1;
    }

    public void part1(List<String> lines) {
        int sum = 0;

        String[] sections = String.join("\n", lines).split("\n\n");
        for (String section : sections) {
            String[] sRows = section.split("\n");
            List<BitSet> rows = new ArrayList<>();
            for (String row : sRows) {
                var set = new BitSet();
                rows.add(set);
                for (int i = 0; i < row.length(); i++) {
                    if (row.charAt(i) == '#') set.set(i);
                }
            }

            List<BitSet> columns = new ArrayList<>();
            int width = sRows[0].length();
            for (int i = 0; i < width; i++) {
                var set = new BitSet();
                columns.add(set);
                for (int j = 0; j < rows.size(); j++) {
                    if (rows.get(j).get(i))
                        set.set(j);
                }
            }

            int horz = findMirror(rows, -1);
            int vert = findMirror(columns, -1);
            if (horz != -1) {
                sum += horz * 100;
            } else if (vert != -1) {
                sum += vert;
            } else {
                throw new IllegalStateException(section);
            }
        }

        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        int sum = 0;

        String[] sections = String.join("\n", lines).split("\n\n");
        outer:
        for (String section : sections) {
            String[] sRows = section.split("\n");
            List<BitSet> rows = new ArrayList<>();
            for (String row : sRows) {
                var set = new BitSet();
                rows.add(set);
                for (int i = 0; i < row.length(); i++) {
                    if (row.charAt(i) == '#') set.set(i);
                }
            }

            List<BitSet> columns = new ArrayList<>();
            int width = sRows[0].length();
            for (int i = 0; i < width; i++) {
                var set = new BitSet();
                columns.add(set);
                for (int j = 0; j < rows.size(); j++) {
                    if (rows.get(j).get(i))
                        set.set(j);
                }
            }

            int ohorz = findMirror(rows, -1);
            int overt = findMirror(columns, -1);

            for (int j = 0; j < sRows.length; j++) {
                for (int i = 0; i < width; i++) {
                    rows.get(j).flip(i);
                    columns.get(i).flip(j);

                    int horz = findMirror(rows, ohorz);
                    int vert = findMirror(columns, overt);
                    if (horz != -1) {
                        sum += horz * 100;
                        continue outer;
                    } else if (vert != -1) {
                        sum += vert;
                        continue outer;
                    }

                    rows.get(j).flip(i);
                    columns.get(i).flip(j);
                }
            }
            throw new IllegalStateException(section);
        }

        System.out.println(sum);
    }

    public static void main(String...args) {
//        new Day13().test(1);
        new Day13().test(2);
        new Day13().run();
    }
}
