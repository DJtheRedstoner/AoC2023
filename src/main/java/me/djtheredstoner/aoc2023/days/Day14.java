package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 implements DayBase {

    BitSet supports = new BitSet();
    BitSet initialRocks = new BitSet();
    int width;
    int height;

    static int[] UP = new int[]{0, -1};
    static int[] DOWN = new int[]{0, 1};
    static int[] LEFT = new int[]{-1, 0};
    static int[] RIGHT = new int[]{1, 0};

    public void init(List<String> lines) {
        width = lines.get(0).length();
        height = lines.size();
        int y = 0;
        for (String line : lines) {
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '#' -> supports.set(y * width + x);
                    case 'O' -> initialRocks.set(y * width + x);
                }
            }
            y++;
        }
    }

    void printData(BitSet rocks) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean rock = rocks.get(y * width + x);
                boolean support = supports.get(y * width + x);
                if (rock && support) throw new IllegalStateException();
                if (rock) {
                    System.out.print('O');
                } else if (support) {
                    System.out.print('#');
                } else {
                    System.out.print('.');
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    void apply(BitSet rocks, int[] direction) {
        while (true) {
            boolean changed = false;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int nX = x + direction[0];
                    int nY = y + direction[1];
                    if (nX < 0 || nX > width - 1 || nY < 0 || nY > height - 1) continue;
                    int pos = y * width + x;
                    int nPos = nY * width + nX;
                    if (rocks.get(pos) && !supports.get(nPos) && !rocks.get(nPos)) {
                        changed = true;
                        rocks.set(nPos);
                        rocks.clear(pos);
                    }
                }
            }
            if (!changed) break;
        }
    }

    public void part1(List<String> lines) {
        var rocks = (BitSet) initialRocks.clone();

        apply(rocks, UP);

//        printData(rocks);

        int sum = 0;
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < width; i++) {
                if (rocks.get(y * width + i)) sum += height - y;
            }
        }
        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        var rocks = (BitSet) initialRocks.clone();

        Map<BitSet, Integer> iters = new HashMap<>();

        int cycles = 1;
        int cycleSize = -1;
        while (true) {
            apply(rocks, UP);
            apply(rocks, LEFT);
            apply(rocks, DOWN);
            apply(rocks, RIGHT);
            if (iters.containsKey(rocks)) {
                cycleSize = cycles - iters.get(rocks);
                cycles = iters.get(rocks);
                break;
            }
            iters.put((BitSet) rocks.clone(), cycles);
            cycles++;
        }

        int e = (1000000000 - cycles) % cycleSize;

        for (int j = 0; j < e; j++) {
            apply(rocks, UP);
            apply(rocks, LEFT);
            apply(rocks, DOWN);
            apply(rocks, RIGHT);
        }

        int sum = 0;
        for (int y = 0; y < height; y++) {
            for (int i = 0; i < width; i++) {
                if (rocks.get(y * width + i)) sum += height - y;
            }
        }
        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day14().test(1);
        new Day14().test(2);
        new Day14().run();
    }
}
