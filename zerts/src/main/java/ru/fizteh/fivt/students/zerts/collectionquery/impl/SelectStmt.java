package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SelectStmt<T, R> {

    private boolean isDistinct;
    private Class returnClass;
    private Function<T, ?>[] functions;
    private List<Pair<T, Integer>> elements;

    private Predicate<T> whereCondition;
    private Comparator<R>[] comparators;
    private Predicate<R> havingCondition;
    private int numberOfObjects;
    private Function<T, ?>[] groupByConditions;

    private CQLComparator<R> cqlComparator;

    @SafeVarargs
    public SelectStmt(List<T> elements, Class<R> returnClass, boolean isDistinct, Function<T, ?>... functions) {
        Integer curr = 0;
        this.elements = new ArrayList<>();
        for (T element : elements) {
            //System.out.println(element.toString());
            this.elements.add(new Pair<T, Integer>(element, curr));
            curr++;
        }
        this.returnClass = returnClass;
        this.isDistinct = isDistinct;
        this.functions = functions;
        this.numberOfObjects = -1;
    }

    public SelectStmt<T, R> where(Predicate<T> predicate) {
        this.whereCondition = predicate;
        return this;
    }

    @SafeVarargs
    public final SelectStmt<T, R> groupBy(Function<T, ?>... expressions) {
        this.groupByConditions = expressions;
        return this;
    }

    @SafeVarargs
    public final SelectStmt<T, R> orderBy(Comparator<R>... comparators) {
        this.comparators = comparators;
        this.cqlComparator = new CQLComparator<R>(comparators);
        return this;
    }

    public SelectStmt<T, R> having(Predicate<R> condition) {
        this.havingCondition = condition;
        return this;
    }

    public SelectStmt<T, R> limit(int amount) {
        this.numberOfObjects = amount;
        return this;
    }

    public Iterable<R> execute() throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        List<R> result = new ArrayList<>();
        Object[] arguments = new Object[functions.length];
        Class[] returnClasses = new Class[functions.length];
        if (whereCondition != null) {
            List<Pair<T, Integer>> filtered = new ArrayList<>();
            elements.stream().filter(element -> whereCondition.test(element.getKey())).forEach(filtered::add);
            elements = filtered;
        }
        if (groupByConditions != null) {
            Map<Object, Integer> mapped = new HashMap<>();
            String[] results = new String[groupByConditions.length];
            List<Pair<T, Integer>> grouped = new ArrayList<>();
            elements.stream().forEach(
                    element -> {
                        for (int i = 0; i < groupByConditions.length; i++) {
                            results[i] = (String) groupByConditions[i].apply(element.getKey());
                        }
                        //System.out.println(results[0]);
                        //System.out.println(mapped);
                        if (!mapped.containsKey(results[0])) {
                            mapped.put(results[0], mapped.size());
                        }
                        grouped.add(new Pair(element.getKey(), mapped.get(results[0])));
                    }
            );
            elements = grouped;
        }
        //System.out.println(elements);
        for (Pair<T, Integer> element : this.elements) {
            //System.out.println(element);
            for (int i = 0; i < functions.length; i++) {
                arguments[i] = functions[i].apply(element.getKey());
                returnClasses[i] = arguments[i].getClass();
            }
            @SuppressWarnings("unchecked")
            R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
            result.add(newElement);
        }
        if (havingCondition != null) {
            List<R> filtered = new ArrayList<>();
            result.stream().filter(havingCondition::test).forEach(filtered::add);
            result = filtered;
        }
        if (comparators != null) {
            result.sort(cqlComparator);
        }
        if (isDistinct) {
            Stream<R> distincted = result.stream().distinct();
            distincted.forEach(result::add);
        }
        if (numberOfObjects != -1) {
            while (result.size() > numberOfObjects) {
                result.remove(result.size() - 1);
            }
        }
        return result;
    }

    public Stream<R> stream() {
        throw new UnsupportedOperationException();
    }

    public UnionStmt union() {
        throw new UnsupportedOperationException();
    }

    public class CQLComparator<K> implements Comparator<K> {
        private Comparator<K>[] comparators;
        @SafeVarargs
        public CQLComparator(Comparator<K>... comparators) {
            this.comparators = comparators;
        }

        @Override
        public int compare(K first, K second) {
            for (Comparator<K> comparator : comparators) {
                if (comparator.compare(first, second) != 0) {
                    return comparator.compare(first, second);
                }
            }
            return 0;
        }
    }

}
