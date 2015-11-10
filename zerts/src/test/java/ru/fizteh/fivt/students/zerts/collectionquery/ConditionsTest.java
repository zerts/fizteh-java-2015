package ru.fizteh.fivt.students.zerts.collectionquery;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static ru.fizteh.fivt.students.zerts.collectionquery.CollectionQuery.Student.student;
import static ru.fizteh.fivt.students.zerts.collectionquery.Conditions.rlike;

/**
 * Created by User on 11.11.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionsTest extends TestCase {
    Function<CollectionQuery.Student, String> function = CollectionQuery.Student::getName;

    @Test
    public void testRlike() throws Exception {
        List<CollectionQuery.Student> exampleList = new ArrayList<>();
        exampleList.add(student("iglina", LocalDate.parse("1986-08-06"), "494"));
        exampleList.add(student("kargaltsev", LocalDate.parse("1986-08-06"), "495"));
        exampleList.add(student("zertsalov", LocalDate.parse("1986-08-06"), "495"));

        assertEquals(rlike(function, ".*ov").test(exampleList.get(0)), false);
        assertEquals(rlike(function, ".*ov").test(exampleList.get(1)), false);
        assertEquals(rlike(function, ".*ov").test(exampleList.get(2)), true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLike() throws Exception {
        Conditions.like(function, "aaa");
    }
}
