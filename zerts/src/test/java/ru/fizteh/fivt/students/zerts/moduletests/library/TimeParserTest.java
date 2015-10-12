package ru.fizteh.fivt.students.zerts.moduletests.library;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class TimeParserTest extends TestCase {

    @Test
    public void testPrintGoneDate() throws Exception {
        LocalDateTime nowTime = LocalDateTime.of(2012, Month.MARCH, 1, 16, 20);
        LocalDateTime givenTime = LocalDateTime.of(2012, Month.FEBRUARY, 29, 4, 19);
        assertEquals("[Вчера] ", TimeParser.printGoneDate(givenTime, nowTime));

        nowTime = LocalDateTime.of(2013, Month.MARCH, 1, 16, 20);
        assertEquals("[366 дней назад] ", TimeParser.printGoneDate(givenTime, nowTime));

        nowTime = LocalDateTime.of(2012, Month.MARCH, 1, 16, 20);
        givenTime = LocalDateTime.of(2012, Month.FEBRUARY, 28, 4, 19);
        assertEquals("[2 дня назад] ", TimeParser.printGoneDate(givenTime, nowTime));

        givenTime = LocalDateTime.of(2013, Month.MARCH, 1, 16, 20);
        assertEquals("[Только что] ", TimeParser.printGoneDate(givenTime, nowTime));

        nowTime = LocalDateTime.of(2016, Month.JANUARY, 1, 0, 10);
        givenTime = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 50);
        assertEquals("[20 минут назад] ", TimeParser.printGoneDate(givenTime, nowTime));

        givenTime = LocalDateTime.of(2015, Month.DECEMBER, 31, 23, 5);
        assertEquals("[Вчера] ", TimeParser.printGoneDate(givenTime, nowTime));

        nowTime = LocalDateTime.of(2016, Month.JANUARY, 1, 2, 10);
        givenTime = LocalDateTime.of(2016, Month.JANUARY, 1, 1, 3);
        assertEquals("[1 час назад] ", TimeParser.printGoneDate(givenTime, nowTime));

        //TimeParser.printGoneDate(Date.from(givenTime.atZone(ZoneId.systemDefault()).toInstant()));
    }
}
