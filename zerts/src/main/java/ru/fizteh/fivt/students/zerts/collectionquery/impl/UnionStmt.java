package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class UnionStmt<T, R> {
    private List<R> pastElements = new ArrayList<>();
    private List<T> elements = new ArrayList<>();

    public UnionStmt(Iterable<R> iterable) {
        for (R curr : iterable) {
            pastElements.add(curr);
        }
    }

    public UnionStmt(Iterable<R> pastElements, Iterable<T> elements) {
        for (R curr : pastElements) {
            this.pastElements.add(curr);
        }
        for (T curr : elements) {
            this.elements.add(curr);
        }
    }

    public UnionStmt<T, R> from(Iterable<T> elements) {
        return new UnionStmt<>(pastElements, elements);
    }


     @SafeVarargs
     public final SelectStmt<T, R> select(Class<R> returnClass, Function<T, ?>... functions) {
        return new SelectStmt<T, R>((List<R>) pastElements, elements, returnClass, false, functions);
    }

    @SafeVarargs
    public final SelectStmt<T, R> selectDistinct(Class<R> returnClass, Function<T, ?>... functions) {
        return new SelectStmt<T, R>((List<R>) pastElements, elements, returnClass, true, functions);
    }
}
