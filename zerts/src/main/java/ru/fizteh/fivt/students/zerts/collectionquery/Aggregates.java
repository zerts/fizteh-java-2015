package ru.fizteh.fivt.students.zerts.collectionquery;

import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Avg;
import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Count;
import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Max;
import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Min;

import java.util.function.Function;

public class Aggregates {
    public static <T, R extends Comparable> Function<T, R> max(Function<T, R> expression) {
        return new Max<>(expression);
    }

    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        return new Min<>(expression);
    }

    public static <T> Function<T, Integer> count(Function<T, ?> expression) {
        return new Count<>(expression);
    }

    public static <T> Function<T, Double> avg(Function<T, ? extends Number> expression) {
        return new Avg<>(expression);
    }

}
