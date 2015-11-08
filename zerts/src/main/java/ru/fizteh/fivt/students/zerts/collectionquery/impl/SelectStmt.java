package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import javafx.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SelectStmt<T, R> {

    private boolean isDistinct;
    private Class<R> returnClass;
    private Function<T, ?>[] functions;
    private List<Pair<T, Integer>> elements;

    private WhereStmt<T, R> conditions;


    @SafeVarargs
    public SelectStmt(List<T> elements, Class<R> returnClass, boolean isDistinct, Function<T, ?>... functions) {
        Integer curr = 0;
        for (T element : elements) {
            this.elements.add(new Pair<T, Integer>(element, curr));
            curr++;
        }
        this.returnClass = returnClass;
        this.isDistinct = isDistinct;
        this.functions = functions;
    }

    public WhereStmt<T, R> where(Predicate<T> predicate) {
        this.conditions = new WhereStmt<T, R>(predicate);
        return conditions;
    }

    public Iterable<R> execute() {
        throw new UnsupportedOperationException();
    }

    public Stream<R> stream() {
        throw new UnsupportedOperationException();
    }

    public class WhereStmt<T, R> {
        public Predicate<T> getWhereCondition() {
            return whereCondition;
        }
        private Predicate<T> whereCondition;

        public Comparator<T>[] getComparators() {
            return comparators;
        }
        private Comparator<T>[] comparators;

        public Predicate<R> getHavingCondition() {
            return havingCondition;
        }
        private Predicate<R> havingCondition;

        public int getNumberOfObjects() {
            return numberOfObjects;
        }
        private int numberOfObjects;

        public Function<T, ?>[] getGroupByConditions() {
            return groupByConditions;
        }
        private Function<T, ?>[] groupByConditions;

        public WhereStmt(Predicate<T> condition) {
            this.whereCondition = condition;
            this.numberOfObjects = -1;
        }

        @SafeVarargs
        public final WhereStmt<T, R> groupBy(Function<T, ?>... expressions) {
            this.groupByConditions = expressions;
            return this;
        }

        @SafeVarargs
        public final WhereStmt<T, R> orderBy(Comparator<T>... comparators) {
            this.comparators = comparators;
            return this;
        }

        public WhereStmt<T, R> having(Predicate<R> condition) {
            this.havingCondition = condition;
            return this;
        }

        public WhereStmt<T, R> limit(int amount) {
            this.numberOfObjects = amount;
            return this;
        }

        public UnionStmt union() {
            throw new UnsupportedOperationException();
        }
    }

}
