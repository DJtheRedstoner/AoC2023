package me.djtheredstoner.aoc2023;

import java.util.List;

public interface DayBase {

    default void test(int part) {
        int day = Integer.parseInt(getClass().getSimpleName().substring("Day".length()));
        List<String> lines = Util.getTestLines(day, part);

        System.out.println("=== Test " + part + " ===");
        if (lines != null) {
            init(lines);
            if (part == 1) {
                part1(lines);
            } else {
                part2(lines);
            }
        } else {
            System.out.println("No test input.");
        }
        System.out.println();
    }

    default void run() {
        int day = Integer.parseInt(getClass().getSimpleName().substring("Day".length()));
        List<String> lines = Util.getLines(day);

        init(lines);
        System.out.println("=== Part 1 ===");
        time(() -> part1(lines));
        System.out.println("=== Part 2 ===");
        time(() -> part2(lines));
    }

    void init(List<String> lines);

    void part1(List<String> lines);

    void part2(List<String> lines);

    private void time(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        long delta = end - start;
        System.out.println("Time: " + delta / 1_000_000.0 + "ms");
    }

}
