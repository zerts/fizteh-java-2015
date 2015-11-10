package ru.fizteh.fivt.students.zerts.collectionquery;

import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Avg;
import ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl.Count;

import java.util.function.Function;

/**
 * Aggregate functions.
 *
 * @author akormushin
 */
public class Aggregates {

    /**
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> max(Function<C, T> expression) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param expression
     * @param <C>
     * @param <T>
     * @return
     */
    public static <C, T extends Comparable<T>> Function<C, T> min(Function<C, T> expression) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> Function<T, Integer> count(Function<T, ?> expression) {
        return new Count<T>(expression);
    }

    /**
     * @param expression
     * @param <T>
     * @return
     */
    public static <T> Function<T, Integer> avg(Function<T, ? extends Number> expression) {
        return new Avg<T>(expression);
    }

}
