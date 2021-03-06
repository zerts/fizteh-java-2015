package ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl;

import java.util.List;
import java.util.function.Function;

/**
 * Created by User on 10.11.2015.
 */
public class Avg<T> implements Aggregator<T, Double> {

    private Function<T, ? extends Number> function;
    public Avg(Function<T, ? extends Number> expression) {
        this.function = expression;
    }

    @Override
    public Double apply(List<T> elements) {
        Double result = 0.0;
        for (T element : elements) {
            result += (Double) function.apply(element);
        }
        return result / elements.size();
    }

    @Override
    public Double apply(T t) {
        return null;
    }
}
