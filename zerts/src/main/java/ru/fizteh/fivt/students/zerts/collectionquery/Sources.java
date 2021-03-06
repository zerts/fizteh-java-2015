package ru.fizteh.fivt.students.zerts.collectionquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sources {

    @SafeVarargs
    public static <T> List<T> list(T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }
}
