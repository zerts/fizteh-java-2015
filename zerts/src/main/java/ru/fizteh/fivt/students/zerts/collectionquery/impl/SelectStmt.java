package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import javafx.util.Pair;
import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Aggregator;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SelectStmt<T, R> {

    private boolean isDistinct;
    private Class returnClass;
    private Function<T, ?>[] functions;
    private List<T> elements;

    private Predicate<T> whereCondition;
    private Comparator<R>[] comparators;
    private Predicate<R> havingCondition;
    private int numberOfObjects;
    private Function<T, ?>[] groupByConditions;

    private CQLComparator<R> cqlComparator;

    @SafeVarargs
    public SelectStmt(List<T> elements, Class<R> returnClass, boolean isDistinct, Function<T, ?>... functions) {
        this.elements = new ArrayList<>();
        for (T element : elements) {
            //System.out.println(element.toString());
            this.elements.add(element);
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
            List<T> filtered = new ArrayList<>();
            elements.stream().filter(whereCondition::test).forEach(filtered::add);
            elements = filtered;
        }
        if (groupByConditions != null) {
            Map<Integer, Integer> mapped = new HashMap<>();
            String[] results = new String[groupByConditions.length];
            List<Pair<T, Integer>> grouped = new ArrayList<>();
            elements.stream().forEach(
                    element -> {
                        for (int i = 0; i < groupByConditions.length; i++) {
                            results[i] = (String) groupByConditions[i].apply(element);
                        }
                        if (!mapped.containsKey(Objects.hash(results))) {
                            mapped.put(Objects.hash(results), mapped.size());
                        }
                        grouped.add(new Pair(element, mapped.get(Objects.hash(results))));
                    }
            );
            List<List<T>> groupedElements = new ArrayList<>();
            for (int i = 0; i < mapped.size(); i++) {
                groupedElements.add(new ArrayList<T>());
            }

            for (Pair<T, Integer> element : grouped) {
                groupedElements.get(element.getValue()).add(element.getKey());
            }
            for (List<T> group : groupedElements) {
                for (int i = 0; i < functions.length; i++) {
                    if (functions[i] instanceof  Aggregator) {
                        arguments[i] = ((Aggregator) functions[i]).apply(group);
                    } else {
                        arguments[i] = functions[i].apply(group.get(0));
                    }
                    returnClasses[i] = arguments[i].getClass();
                }
                @SuppressWarnings("unchecked")
                R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                result.add(newElement);
            }
        } else {
            for (T element : this.elements) {
                for (int i = 0; i < functions.length; i++) {
                    arguments[i] = functions[i].apply(element);
                    if (functions[i] instanceof  Aggregator) {
                        arguments[i] = null;
                    } else {
                        arguments[i] = functions[i].apply(element);
                    }
                    returnClasses[i] = arguments[i].getClass();
                }
                @SuppressWarnings("unchecked")
                R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
                    result.add(newElement);
            }
        }
        if (havingCondition != null) {
            List<R> filtered = new ArrayList<>();
            result.stream().filter(havingCondition::test).forEach(filtered::add);
            result = filtered;
        }
        if (isDistinct) {
            Set<Integer> hashes = new HashSet<>();
            List<Integer> flags = new ArrayList<>();
            for (R element : result) {
                if (!hashes.contains(element.toString().hashCode())) {
                    flags.add(1);
                    hashes.add(element.toString().hashCode());
                } else {
                    flags.add(0);
                }
            }
            List<R> distincted = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                if (flags.get(i) == 1) {
                    distincted.add(result.get(i));
                }
            }
            result = distincted;
        }
        if (comparators != null) {
            result.sort(cqlComparator);
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