package com.cdy.demo.java.effectiveJava3;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 第37条
 */
public class EnumMapTest {


    public static void main(String[] args) {
        HashSet<Plant> garden = new HashSet<>();

        Map<Plant.LifeCycle, Set<Plant>> plantsBylifeCycle =
                new EnumMap<>(Plant.LifeCycle.class);
        for (Plant.LifeCycle lifeCycle : Plant.LifeCycle.values())
            plantsBylifeCycle.put(lifeCycle, new HashSet<>());

        for (Plant p : garden)
            plantsBylifeCycle.get(p.lifeCycle).add(p);

        System.out.println(plantsBylifeCycle);
    }

}

class Plant {
    enum LifeCycle {ANNUAL, PERENNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;

    Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;

    }

    @Override
    public String toString() {
        return name;
    }
}

enum Phase {
    SOLID, LIQUID, GAS;

    /**
     * 编译器无法知道序数和数组索引之间的关系。如果在过渡表中出
     * 了错，或者在修改Phase 或者Phase.Transition 枚举类型的时候忘记将它更新，程
     * 序就会在运行时失败。这种失败的形式可能为ArrayindexOutOfBoundsExcept 工0口、
     * NullPointerException 或者（更糟糕的是）没有任何提示的错误行为。这张表的大小
     * 是阶段数的平方，即使非空项的数量比较少。
     */
    public enum Tansition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;
        private static final Tansition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        public static Tansition from(Phase from, Phase to) {
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }

    }
}

enum Phase1 {
    SOLID1, LIQUID1, GAS1,
    ;

    public enum Tansition1 {
        MELT(SOLID1, LIQUID1), FREEZE(LIQUID1, SOLID1),
        BOIL(LIQUID1, GAS1), CONDENSE(GAS1, LIQUID1),
        SUBLIME(SOLID1, GAS1), DEPOSIT(GAS1, SOLID1);

        private final Phase1 from;
        private final Phase1 to;

        Tansition1(Phase1 from, Phase1 to) {
            this.from = from;
            this.to = to;
        }

        private static final Map<Phase1, Map<Phase1, Tansition1>>
                m = Stream.of(values())
                .collect(Collectors
                        .groupingBy(t -> t.from,
                                Collectors.toMap(e -> e.to, t -> t, (x, y) -> y,
                                        () -> new EnumMap<>(Phase1.class))));

        public static Tansition1 from(Phase1 from, Phase1 to) {
            return m.get(from).get(to);
        }
    }

}