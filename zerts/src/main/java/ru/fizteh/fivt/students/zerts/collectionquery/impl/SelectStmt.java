package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import javafx.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SelectStmt<T, R> {

    private boolean isDistinct;
    private Class returnClass;
    private Function<T, ?>[] functions;
    private List<Pair<T, Integer>> elements;

    private Predicate<T> whereCondition;
    private Comparator<T>[] comparators;
    private Predicate<R> havingCondition;
    private int numberOfObjects;
    private Function<T, ?>[] groupByConditions;

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
    public final SelectStmt<T, R> orderBy(Comparator<T>... comparators) {
        this.comparators = comparators;
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
            throw new UnsupportedOperationException();
        }
        if (comparators != null) {
            throw new UnsupportedOperationException();
        }
        if (numberOfObjects != -1) {
            while (elements.size() > numberOfObjects) {
                elements.remove(elements.size() - 1);
            }
        }
        for (Pair<T, Integer> element : this.elements) {
            //System.out.println(element);
            for (int i = 0; i < functions.length; i++) {
                arguments[i] = functions[i].apply(element.getKey());
                returnClasses[i] = arguments[i].getClass();
            }
            R newElement = (R) returnClass.getConstructor(returnClasses).newInstance(arguments);
            result.add(newElement);
        }
        return result;
    }

    public Stream<R> stream() {
        throw new UnsupportedOperationException();
    }

    public UnionStmt union() {
        throw new UnsupportedOperationException();
    }
}
