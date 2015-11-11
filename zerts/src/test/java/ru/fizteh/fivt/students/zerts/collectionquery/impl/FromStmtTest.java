package ru.fizteh.fivt.students.zerts.collectionquery.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ru.fizteh.fivt.students.zerts.collectionquery.CollectionQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by User on 11.11.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class FromStmtTest extends TestCase {

    List<CollectionQuery.Student> exampleList, emptyExampleList;
    Function<CollectionQuery.Student, Double> functionAge;
    Function<CollectionQuery.Student, String> functionName, functionGroup;
    CollectionQuery.Student student;

    @Before
    public void setUp() throws Exception {
        exampleList = new ArrayList<>();
        emptyExampleList = new ArrayList<>();
        exampleList.add(new CollectionQuery.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        exampleList.add(new CollectionQuery.Student("kargaltsev", LocalDate.parse("1997-02-20"), "495"));
        exampleList.add(new CollectionQuery.Student("zertsalov", LocalDate.parse("1996-10-29"), "495"));
        functionAge = CollectionQuery.Student::age;
        functionName = CollectionQuery.Student::getName;
        functionGroup = CollectionQuery.Student::getGroup;
        student = new CollectionQuery.Student("zertsalov", LocalDate.parse("1996-10-29"), "495");
    }

    @Test
    public void testFrom() throws Exception {

    }

    @Test
    public void testSelect() throws Exception {

    }

    @Test
    public void testSelectDistinct() throws Exception {

    }
}
