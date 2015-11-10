package ru.fizteh.fivt.students.zerts.collectionquery.aggregatesImpl;

import java.util.List;
import java.util.function.Function;

/**
 * Created by User on 10.11.2015.
 */
public interface Aggregator<T, C> extends Function<T, C> {
    C apply(List<T> elements);
}
