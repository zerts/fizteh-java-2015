package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FromStmt<T> {
    private List<T> elements = new ArrayList<T>();

    protected FromStmt(Iterable<T> iterable) {
        for (T curr : iterable) {
            elements.add(curr);
        }
    }

    public static <T> FromStmt<T> from(Iterable<T> iterable) {
        return new FromStmt<>(iterable);
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> select(Class<R> returnClass, Function<T, ?>... functions) {
        return new SelectStmt<>(elements, returnClass, false, functions);
    }

    @SafeVarargs
    public final <R> SelectStmt<T, R> selectDistinct(Class<R> returnClass, Function<T, ?>... functions) {
        return new SelectStmt<>(elements, returnClass, true, functions);
    }
}
