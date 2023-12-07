package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day7 implements DayBase {

    static List<Character> CARDS = Arrays.asList('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
    static List<Character> CARDS2 = Arrays.asList('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J');

    List<Hand> hands = new ArrayList<>();

    static int count(String s, char c) {
        int n = 0;
        for (char c1 : s.toCharArray()) {
            if (c1 == c) n++;
        }
        return n;
    }

    static int count(List<Integer> s, int c) {
        int n = 0;
        for (Integer i : s) {
            if (i == c) n++;
        }
        return n;
    }

    record Hand(String hand, int bid) {
        int getStrength() {
            Map<String, Integer> c = new HashMap<>();
            List<Integer> counts = new ArrayList<>();
            for (char card : CARDS) {
                int count = count(hand, card);
                c.put(String.valueOf(card), count);
                counts.add(count);
            }
            if (counts.contains(5)) {
                return 8;
            }
            if (counts.contains(4)) {
                return 7;
            }
            if (counts.contains(3) && counts.contains(2)) {
                return 6;
            }
            if (counts.contains(3)) {
                return 5;
            }
            if (count(counts, 2) == 2) {
                return 4;
            }
            if (count(counts, 2) == 1) {
                return 3;
            }
            if (count(counts, 1) == 5) {
                return 2;
            }
            throw new IllegalStateException(hand);
        }

        int getStrengthJokers() {
            Map<String, Integer> c = new HashMap<>();
            List<Integer> counts = new ArrayList<>();

            for (char card : CARDS) {
                int count = count(hand, card);
                c.put(String.valueOf(card), count);
                if (card != 'J')
                    counts.add(count);
            }

            int jokers = count(hand, 'J');
            int maxCount = counts.stream().max(Integer::compare).orElseThrow();

            if (maxCount + jokers == 5) {
                return 8;
            }
            if (maxCount + jokers == 4) {
                return 7;
            }
            if ((maxCount + jokers == 3 && count(counts, 2) == 2) || counts.contains(3) && (counts.contains(2) || jokers > 0)) {
                return 6;
            }
            if (maxCount + jokers == 3) {
                return 5;
            }
            if (maxCount == 2 && jokers > 0 || count(counts, 2) == 2) {
                return 4;
            }
            if (maxCount == 2 || maxCount == 1 && jokers > 0) {
                return 3;
            }
            if (maxCount != 1) throw new IllegalStateException(hand);
//            if (count(counts, 1) == 5) {
            return 2;
//            }
//            throw new IllegalStateException(hand);
        }
    }

    public void init(List<String> lines) {
        for (String line : lines) {
            String[] s = line.split(" ");
            hands.add(new Hand(s[0], Integer.parseInt(s[1])));
        }
    }

    public void part1(List<String> lines) {
        List<Hand> sorted = new ArrayList<>(hands);
        sorted.sort((a, b) -> {
            int aStrength = a.getStrength();
            int bStrength = b.getStrength();
            if (aStrength != bStrength) {
                return aStrength - bStrength;
            }

            for (int i = 0; i < 5; i++) {
                int idxA = CARDS.indexOf(a.hand.charAt(i));
                int idxB = CARDS.indexOf(b.hand.charAt(i));
                if (idxA == idxB) continue;
                return idxB - idxA;
            }
            throw new IllegalStateException();
        });

        int sum = 0;

        for (int i = 0; i < sorted.size(); i++) {
            sum += sorted.get(i).bid * (i + 1);
        }

        System.out.println(sum);
    }

    public void part2(List<String> lines) {
        List<Hand> sorted = new ArrayList<>(hands);
        sorted.sort((a, b) -> {
            int aStrength = a.getStrengthJokers();
            int bStrength = b.getStrengthJokers();
            if (aStrength != bStrength) {
                return aStrength - bStrength;
            }

            for (int i = 0; i < 5; i++) {
                int idxA = CARDS2.indexOf(a.hand.charAt(i));
                int idxB = CARDS2.indexOf(b.hand.charAt(i));
                if (idxA == idxB) continue;
                return idxB - idxA;
            }
            throw new IllegalStateException();
        });


        int sum = 0;

        for (int i = 0; i < sorted.size(); i++) {
            System.out.println(sorted.get(i) + " " + sorted.get(i).getStrengthJokers());
            sum += sorted.get(i).bid * (i + 1);
        }

        System.out.println(sum);
    }

    public static void main(String...args) {
        new Day7().test(1);
        new Day7().test(2);
        new Day7().run();
    }
}
