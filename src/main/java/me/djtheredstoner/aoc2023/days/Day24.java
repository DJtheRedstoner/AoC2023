package me.djtheredstoner.aoc2023.days;

import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BitVecNum;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Symbol;
import me.djtheredstoner.aoc2023.DayBase;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day24 implements DayBase {

    public void init(List<String> lines) {

    }

    record Stone(long x, long y, long z, long vX, long vY, long vZ) {}

    public void part1(List<String> lines) {
        List<Stone> stones = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(" @ ");
            long[] left = Arrays.stream(parts[0].split(", ")).mapToLong(it -> Long.parseLong(it.trim())).toArray();
            long[] right = Arrays.stream(parts[1].split(", ")).mapToLong(it -> Long.parseLong(it.trim())).toArray();
            stones.add(new Stone(
                left[0], left[1], left[2],
                right[0], right[1], right[2]
            ));
        }

        int count = 0;

        for (Stone stone : stones) {
            for (Stone other : stones) {
                double m1 = (double) stone.vY / stone.vX;
                double b1 = stone.y - stone.x * m1;

                double m2 = (double) other.vY / other.vX;
                double b2 = other.y - other.x * m2;

                double m = m1 - m2;
                double b = b1 - b2;

                if (m == 0) continue;
                double x = -b / m;
                double y = m1 * x + b1;

                double t1 = (x - stone.x()) / stone.vX();
                double t2 = (x - other.x()) / other.vX();

                if (t1 < 0 || t2 < 0) continue;

                if (x >= 200000000000000.0 && x <= 400000000000000.0 && y >= 200000000000000.0 && y <= 400000000000000.0) {
                    count++;
                }
            }

        }

        System.out.println(count / 2);
    }

    public void part2(List<String> lines) {
        List<Stone> stones = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(" @ ");
            long[] left = Arrays.stream(parts[0].split(", ")).mapToLong(it -> Long.parseLong(it.trim())).toArray();
            long[] right = Arrays.stream(parts[1].split(", ")).mapToLong(it -> Long.parseLong(it.trim())).toArray();
            stones.add(new Stone(
                left[0], left[1], left[2],
                right[0], right[1], right[2]
            ));
        }

        Context ctx = new Context();

        var x = ctx.mkIntConst("x");
        var y = ctx.mkIntConst("y");
        var z = ctx.mkIntConst("z");
        var vx = ctx.mkIntConst("vx");
        var vy = ctx.mkIntConst("vy");
        var vz = ctx.mkIntConst("vz");

        Solver s = ctx.mkSolver();

        for (int i = 0; i < stones.size(); i++) {
            Stone st = stones.get(i);

            var t = ctx.mkIntConst("t_" + i);

            s.add(ctx.mkGe(t, ctx.mkInt(0)));
            s.add(ctx.mkEq(
                ctx.mkAdd(x, ctx.mkMul(vx, t)),
                ctx.mkAdd(ctx.mkInt(st.x()), ctx.mkMul(ctx.mkInt(st.vX()), t))
            ));
            s.add(ctx.mkEq(
                ctx.mkAdd(y, ctx.mkMul(vy, t)),
                ctx.mkAdd(ctx.mkInt(st.y()), ctx.mkMul(ctx.mkInt(st.vY()), t))
            ));
            s.add(ctx.mkEq(
                ctx.mkAdd(z, ctx.mkMul(vz, t)),
                ctx.mkAdd(ctx.mkInt(st.z()), ctx.mkMul(ctx.mkInt(st.vZ()), t))
            ));
        }

        System.out.println(s.check());
        System.out.println(
            ((IntNum)s.getModel().eval(x, false)).getInt64() +
            ((IntNum)s.getModel().eval(y, false)).getInt64() +
            ((IntNum)s.getModel().eval(z, false)).getInt64()
        );
    }

    public static void main(String...args) {
        new Day24().test(1);
        new Day24().test(2);
        new Day24().run();
    }
}
