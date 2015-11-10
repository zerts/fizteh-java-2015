package ru.fizteh.fivt.students.zerts.collectionquery;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.fizteh.fivt.students.zerts.collectionquery.CollectionQuery.Student.student;

@RunWith(MockitoJUnitRunner.class)
public class SourcesTest extends TestCase {

    @Test
    public void testList() throws Exception {
        List<CollectionQuery.Student> exampleList = new ArrayList<>();
        exampleList.add(student("iglina", LocalDate.parse("1986-08-06"), "494"));
        exampleList.add(student("kargaltsev", LocalDate.parse("1986-08-06"), "495"));
        exampleList.add(student("zertsalov", LocalDate.parse("1986-08-06"), "495"));

        List<CollectionQuery.Student> resultList = Sources.list(
                        student("iglina", LocalDate.parse("1986-08-06"), "494"),
                        student("kargaltsev", LocalDate.parse("1986-08-06"), "495"),
                        student("zertsalov", LocalDate.parse("1986-08-06"), "495"));
        assertEquals(exampleList.size(), resultList.size());
        for (int i = 0; i < exampleList.size(); i++) {
            assertEquals(exampleList.get(i).toString(), resultList.get(i).toString());
        }
    }
}
