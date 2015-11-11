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
public class UnionStmtTest extends TestCase {

    List<CollectionQuery.Student> exampleList, emptyExampleList, pastExampleList;
    Function<CollectionQuery.Student, Double> functionAge;
    Function<CollectionQuery.Student, String> functionName, functionGroup;
    CollectionQuery.Student student;

    @Before
    public void setUp() throws Exception {
        exampleList = new ArrayList<>();
        emptyExampleList = new ArrayList<>();
        pastExampleList = new ArrayList<>();
        exampleList.add(new CollectionQuery.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        exampleList.add(new CollectionQuery.Student("kargaltsev", LocalDate.parse("1997-02-20"), "495"));
        exampleList.add(new CollectionQuery.Student("zertsalov", LocalDate.parse("1996-10-29"), "495"));
        pastExampleList.add(new CollectionQuery.Student("iglina", LocalDate.parse("1996-08-06"), "494"));
        functionAge = CollectionQuery.Student::age;
        functionName = CollectionQuery.Student::getName;
        functionGroup = CollectionQuery.Student::getGroup;
        student = new CollectionQuery.Student("zertsalov", LocalDate.parse("1996-10-29"), "495");
    }

    @Test
    public void testFrom() throws Exception {
        UnionStmt<CollectionQuery.Student, CollectionQuery.Student> unionStmt = new UnionStmt(exampleList);
        unionStmt = unionStmt.from(exampleList);
        assertEquals(unionStmt.getElements().size(), exampleList.size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(unionStmt.getElements().get(i), exampleList.get(i));
        }

        assertEquals(unionStmt.getPastElements().size(), exampleList.size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(unionStmt.getPastElements().get(i), exampleList.get(i));
        }
    }

    @Test
    public void testSelect() throws Exception {
        UnionStmt<CollectionQuery.Student, CollectionQuery.Student> unionStmt = new UnionStmt(pastExampleList);
        SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select = unionStmt.from(exampleList)
                .select(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                        CollectionQuery.Student::getGroup);
        assertEquals(select.getNumberOfObjects(), -1);
        assertEquals(select.getReturnClass(), CollectionQuery.Student.class);
        assertEquals(select.isDistinct(), false);
        assertEquals(select.isUnion(), true);
        Function<CollectionQuery.Student, String>[] functions = new Function[2];
        functions[0] = CollectionQuery.Student::getName;
        functions[1] = CollectionQuery.Student::getGroup;
        assertEquals(select.getFunctions().length, functions.length);
        for (int i = 0; i < functions.length; i++) {
            for (CollectionQuery.Student element : exampleList) {
                assertEquals(functions[i].apply(element),
                        select.getFunctions()[i].apply(element));
            }
        }
        assertEquals(exampleList.size(), select.getElements().size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(exampleList.get(i), select.getElements().get(i));
        }
        assertEquals(select.getPastElements().size(), pastExampleList.size());
        for (int i = 0; i < pastExampleList.size(); i++) {
            assertEquals(pastExampleList.get(i), select.getPastElements().get(i));
        }
    }

    @Test
    public void testSelectDistinct() throws Exception {
        UnionStmt<CollectionQuery.Student, CollectionQuery.Student> unionStmt = new UnionStmt(pastExampleList);
        SelectStmt<CollectionQuery.Student, CollectionQuery.Student> select = unionStmt.from(exampleList)
                .selectDistinct(CollectionQuery.Student.class, CollectionQuery.Student::getName,
                        CollectionQuery.Student::getGroup);
        assertEquals(select.getNumberOfObjects(), -1);
        assertEquals(select.getReturnClass(), CollectionQuery.Student.class);
        assertEquals(select.isDistinct(), true);
        assertEquals(select.isUnion(), true);
        Function<CollectionQuery.Student, String>[] functions = new Function[2];
        functions[0] = CollectionQuery.Student::getName;
        functions[1] = CollectionQuery.Student::getGroup;
        assertEquals(select.getFunctions().length, functions.length);
        for (int i = 0; i < functions.length; i++) {
            for (CollectionQuery.Student element : exampleList) {
                assertEquals(functions[i].apply(element),
                        select.getFunctions()[i].apply(element));
            }
        }
        assertEquals(exampleList.size(), select.getElements().size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(exampleList.get(i), select.getElements().get(i));
        }
        assertEquals(select.getPastElements().size(), pastExampleList.size());
        for (int i = 0; i < pastExampleList.size(); i++) {
            assertEquals(pastExampleList.get(i), select.getPastElements().get(i));
        }
    }
}
