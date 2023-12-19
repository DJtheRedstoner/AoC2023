package me.djtheredstoner.aoc2023.days;

import me.djtheredstoner.aoc2023.DayBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 implements DayBase {

    public void init(List<String> lines) {

    }

    enum Op {
        GT, LT;

        static Op fromString(String s) {
            return switch (s) {
                case "<" -> LT;
                case ">" -> GT;
                default -> throw new IllegalArgumentException(s);
            };
        }
    }

    record Workflow(String name, List<Expression> expr, String def) {}
    record Expression(String var, Op op, int cond, String res) {}

    boolean process(Map<String, Integer> vars, Map<String, Workflow> workflows, String name) {
        if (name.equals("A")) return true;
        if (name.equals("R")) return false;

        Workflow w = workflows.get(name);
        for (Expression expression : w.expr()) {
            int value = vars.get(expression.var);
            int cmp = expression.cond;
            if (expression.op == Op.LT && value < cmp || expression.op == Op.GT && value > cmp) {
                return process(vars, workflows, expression.res);
            }
        }
        return process(vars, workflows, w.def);
    }

    public void part1(List<String> lines) {
        Map<String, Workflow> workflows = new HashMap<>();

        String[] sections = String.join("\n", lines).split("\n\n");

        for (String line : sections[0].split("\n")) {
            String name = line.substring(0, line.indexOf("{"));
            String[] parts = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");
            List<Expression> exprs = new ArrayList<>();
            for (int i = 0; i < parts.length - 1; i++) {
                String d = parts[i];
                Pattern p = Pattern.compile("(\\w+)([<>])(\\d+):(\\w+)");
                Matcher m = p.matcher(d);
                m.find();
                exprs.add(new Expression(m.group(1), Op.fromString(m.group(2)), Integer.parseInt(m.group(3)), m.group(4)));
            }
            String def = parts[parts.length - 1];
            workflows.put(name, new Workflow(name, exprs, def));
        }

        int sum = 0;

        for (String s : sections[1].split("\n")) {
            String[] vars = s.substring(s.indexOf("{") + 1, s.indexOf("}")).split(",");
            Map<String, Integer> v = new HashMap<>();
            for (String var : vars) {
                String[] d = var.split("=");
                v.put(d[0], Integer.parseInt(d[1]));
            }

            if (process(v, workflows, "in")) {
                sum += v.values().stream().mapToInt(it -> it).sum();
            }
        }

        System.out.println(sum);
    }

    record Range(Set<Integer> values) {
        static Range from(int min, int max) {
            return new Range(IntStream.range(min, max + 1).boxed().collect(Collectors.toSet()));
        }

        Range sub(Range other) {
            Set<Integer> newValues = new HashSet<>(values);
            newValues.removeAll(other.values());
            return new Range(newValues);
        }
    }

    record Vars(Range x, Range m, Range a, Range s) {
        Range get(String s) {
            return switch (s) {
                case "x" -> x();
                case "m" -> m();
                case "a" -> a();
                case "s" -> s();
                default -> throw new IllegalArgumentException(s);
            };
        }

        Vars set(String s, Range r) {
            return switch (s) {
                case "x" -> new Vars(r, m(), a(), s());
                case "m" -> new Vars(x(), r, a(), s());
                case "a" -> new Vars(x(), m(), r, s());
                case "s" -> new Vars(x(), m(), a(), r);
                default -> throw new IllegalArgumentException(s);
            };
        }

        Collection<Range> values() {
            return List.of(x, m, a, s);
        }
    }

    public void part2(List<String> lines) {
        Map<String, Workflow> workflows = new HashMap<>();

        String[] sections = String.join("\n", lines).split("\n\n");

        for (String line : sections[0].split("\n")) {
            String name = line.substring(0, line.indexOf("{"));
            String[] parts = line.substring(line.indexOf("{") + 1, line.indexOf("}")).split(",");
            List<Expression> exprs = new ArrayList<>();
            for (int i = 0; i < parts.length - 1; i++) {
                String d = parts[i];
                Pattern p = Pattern.compile("(\\w+)([<>])(\\d+):(\\w+)");
                Matcher m = p.matcher(d);
                m.find();
                exprs.add(new Expression(m.group(1), Op.fromString(m.group(2)), Integer.parseInt(m.group(3)), m.group(4)));
            }
            String def = parts[parts.length - 1];
            workflows.put(name, new Workflow(name, exprs, def));
        }

        List<Vars> acceptedRanges = new ArrayList<>();

        reduce(acceptedRanges, workflows, "in", new Vars(
            Range.from(1, 4000),
            Range.from(1, 4000),
            Range.from(1, 4000),
            Range.from(1, 4000)
        ));

//        System.out.println(acceptedRanges);

//        long total = acceptedRanges.stream().mapToLong(vars -> vars.values().stream().mapToLong(it -> it.values().size()).reduce(1, (a, b) -> a * b)).sum();

        long total = 0;
        for (Vars acceptedRange : acceptedRanges) {
            long combos = 1;
            for (Range value : acceptedRange.values()) {
                combos *= value.values().size();
            }
            total += combos;
        }

        System.out.println(total);
    }

    void reduce(List<Vars> acceptedRanges, Map<String, Workflow> workflows, String workflow, Vars vars) {
        if (workflow.equals("A")) {
//            System.out.println(vars);
            acceptedRanges.add(vars);
            return;
        }
        if (workflow.equals("R")) return;

        for (Expression expression : workflows.get(workflow).expr()) {
            Range currRange = vars.get(expression.var);
            int cmp = expression.cond;
            var less = Range.from(1, cmp);
            var more = Range.from(cmp, 4000);

            Range newRange;
            if (expression.op == Op.LT) {
                newRange = currRange.sub(more);
            } else {
                newRange = currRange.sub(less);
            }

            Vars newVars = vars.set(expression.var, newRange);
            reduce(acceptedRanges, workflows, expression.res, newVars);

            vars = vars.set(expression.var, currRange.sub(newRange));
        }

        reduce(acceptedRanges, workflows, workflows.get(workflow).def, vars);
    }

    public static void main(String...args) {
        new Day19().test(1);
        new Day19().test(2);
        new Day19().run();
    }
}
