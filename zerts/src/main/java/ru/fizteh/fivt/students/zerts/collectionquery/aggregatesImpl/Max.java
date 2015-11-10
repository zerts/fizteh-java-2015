package ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl;

import java.util.List;
import java.util.function.Function;

/**
 * Created by User on 11.11.2015.
 */
public class Max<T, R extends Comparable<R>> implements Aggregator<T, R> {

    private Function<T, R> function;
    public Max(Function<T, R> expression) {
        this.function = expression;
    }

    @Override
    public R apply(List<T> elements) {
        if (elements.isEmpty()) {
            return null;
        }
        T result = elements.get(0);
        for (T element : elements) {
            if (function.apply(result).compareTo(function.apply(element)) == -1) {
                result = element;
            }
        }
        return function.apply(result);
    }

    @Override
    public R apply(T t) {
        return null;
    }
}
