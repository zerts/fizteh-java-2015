package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UnionStmt<T, R> {

    private List<R> pastElements = new ArrayList<>();

    private boolean isTuple;

    public UnionStmt(Iterable<R> iterable) {
        for (R curr : iterable) {
            pastElements.add(curr);
        }
        this.isTuple = false;
    }

    public UnionStmt(Iterable<R> iterable, boolean isTuple) {
        for (R curr : iterable) {
            pastElements.add(curr);
        }
        this.isTuple = true;
    }

    public <S> FromClause<S, R> from(Iterable<S> elements) {
        if (isTuple) {
            return new FromClause<>(pastElements, elements);
        } else {
            return new FromClause<>(pastElements, /*(Iterable<T>)*/ elements);
        }
    }


    public class FromClause<S, Q> {
        private List<Q> pastElements = new ArrayList<>();

        private List<S> elements = new ArrayList<>();

        public FromClause(Iterable<Q> pastElements, Iterable<S> elements) {
            for (Q curr : pastElements) {
                this.pastElements.add(curr);
            }
            for (S curr : elements) {
                this.elements.add(curr);
            }
        }
        @SafeVarargs
        public final SelectStmt<S, Q> select(Class<Q> returnClass, Function<S, ?>... functions) {
            return new SelectStmt<>(pastElements, elements, returnClass, false, functions);
        }

        public final <F, Z> SelectStmt<S, Tuple<F, Z>> select(Function<S, F> first, Function<S, Z> second) {
            return new SelectStmt<>((List<Tuple<F, Z>>) pastElements, elements, false, first, second);
        }

        @SafeVarargs
        public final SelectStmt<S, Q> selectDistinct(Class<Q> returnClass, Function<S, ?>... functions) {
            return new SelectStmt<>(pastElements, elements, returnClass, true, functions);
        }

        public <J> JoinClause<Q, S, J> join(Iterable<J> iterable) {
            return new JoinClause<>(pastElements, elements, iterable);
        }
    }


    public class JoinClause<W, F, J> {

        private List<F> firstElements = new ArrayList<>();
        private List<J> secondElements = new ArrayList<>();
        private List<W> pastElements = new ArrayList<>();
        private List<Tuple<F, J>> elements = new ArrayList<>();

        public JoinClause(List<W> pastElements, List<F> firstElements, Iterable<J> secondElements) {
            this.pastElements.addAll(pastElements.stream().collect(Collectors.toList()));
            this.firstElements.addAll(firstElements.stream().collect(Collectors.toList()));
            for (J curr : secondElements) {
                this.secondElements.add(curr);
            }
            //secondElements.forEach(System.out::print);
        }

        public FromClause<Tuple<F, J>, W> on(BiPredicate<F, J> condition) {
            for (F first : firstElements) {
                for (J second : secondElements) {
                    if (condition.test(first, second)) {
                        elements.add(new Tuple<>(first, second));
                    }
                }
            }
            //System.out.println(secondElements.get(0));
            //System.out.println(elements.get(0));
            return new FromClause<>(pastElements, elements);
        }

        public <K extends Comparable<?>> FromClause<Tuple<F, J>, W> on(
                Function<F, K> leftKey,
                Function<J, K> rightKey) {
            HashMap<K, List<J>> map = new HashMap<>();
            for (J element : secondElements) {
                K key = rightKey.apply(element);
                if (!map.containsKey(key)) {
                    map.put(key, new ArrayList<>());
                }
                map.get(key).add(element);
            }
            for (F first : firstElements) {
                K key = leftKey.apply(first);
                if (map.containsKey(key)) {
                    List<J> second = map.get(key);
                    second.forEach(s -> elements.add(new Tuple<>(first, s)));
                }
            }
            return new FromClause<>(pastElements, elements);
        }
    }
}
