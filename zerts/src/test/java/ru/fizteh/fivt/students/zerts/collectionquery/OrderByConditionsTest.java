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

/**
 * Created by User on 11.11.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderByConditionsTest extends TestCase {
    Function<CollectionQuery.Student, String> function = CollectionQuery.Student::getName;

    @Test
    public void testAsc() throws Exception {
        List<CollectionQuery.Student> exampleList = new ArrayList<>();
        exampleList.add(student("iglina", LocalDate.parse("1986-08-06"), "494"));
        exampleList.add(student("kargaltsev", LocalDate.parse("1986-08-06"), "495"));
        exampleList.add(student("zertsalov", LocalDate.parse("1986-08-06"), "495"));
        assertTrue(OrderByConditions.asc(function).compare(exampleList.get(0), exampleList.get(2)) < 0);
        assertTrue(OrderByConditions.asc(function).compare(exampleList.get(1), exampleList.get(0)) > 0);
        assertTrue(OrderByConditions.asc(function).compare(exampleList.get(0), exampleList.get(0)) == 0);
    }

    @Test
    public void testDesc() throws Exception {
        List<CollectionQuery.Student> exampleList = new ArrayList<>();
        exampleList.add(student("iglina", LocalDate.parse("1986-08-06"), "494"));
        exampleList.add(student("kargaltsev", LocalDate.parse("1986-08-06"), "495"));
        exampleList.add(student("zertsalov", LocalDate.parse("1986-08-06"), "495"));
        assertTrue(OrderByConditions.desc(function).compare(exampleList.get(0), exampleList.get(2)) > 0);
        assertTrue(OrderByConditions.desc(function).compare(exampleList.get(1), exampleList.get(0)) < 0);
        assertTrue(OrderByConditions.desc(function).compare(exampleList.get(0), exampleList.get(0)) == 0);
    }
}
