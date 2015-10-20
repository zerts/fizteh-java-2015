package ru.fizteh.fivt.students.zerts.moduletests.library;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeParser {
    static final int MIN_MODE = 0;
    static final int HOUR_MODE = 1;
    static final int DAY_MODE = 2;
    static final int RT_MODE = 3;

    static final int FIRST_FORM = 0;
    static final int SECOND_FORM = 1;
    static final int THIRD_FORM = 2;

    static final int TEN_MOD = 10;
    static final int FIVE = 5;
    static final int HUNDRED_MOD = 100;
    static final int TWENTY = 20;

    static final Duration JUST_NOW_TIME = Duration.ofMinutes(2L);

    static final String[][] WORDS = {
            {"минут", "минута", "минуты"},
            {"часов", "час", "часа"},
            {"дней", "день", "дня"},
            {"ретвитов", "ретвит", "ретвита"}
    };

    public static String rightWordPrinting(long goneTime, int mode) {
        StringBuilder result = new StringBuilder().append(String.valueOf(goneTime)).append(" ");
        if (goneTime % TEN_MOD >= FIVE || goneTime % TEN_MOD == 0 || (goneTime % HUNDRED_MOD > TEN_MOD
                && goneTime % HUNDRED_MOD < TWENTY)) {
            result.append(WORDS[mode][FIRST_FORM]);
        } else if (goneTime % TEN_MOD == 1) {
            result.append(WORDS[mode][SECOND_FORM]);
        } else {
            result.append(WORDS[mode][THIRD_FORM]);
        }
        if (mode < RT_MODE) {
            result.append(" назад");
        }
        return result.toString();
    }

    public static String printGoneDate(Date givenDate) {
        return printGoneDate(LocalDateTime.ofInstant(givenDate.toInstant(), ZoneId.systemDefault()),
                LocalDateTime.now());
    }

    public static String printGoneDate(LocalDateTime givenTime, LocalDateTime nowTime) {
        Duration goneTime = Duration.between(givenTime, nowTime);
        StringBuilder result = new StringBuilder().append("[");
        if (goneTime.compareTo(JUST_NOW_TIME) < 0) {
            result.append("Только что");
        } else if (goneTime.compareTo(Duration.ofHours(1)) < 0) {
            result.append(rightWordPrinting(goneTime.toMinutes(), MIN_MODE));
        } else if (givenTime.toLocalDate().equals(nowTime.toLocalDate())) {
            result.append(rightWordPrinting(goneTime.toHours(), HOUR_MODE));
        } else if (ChronoUnit.DAYS.between(givenTime.toLocalDate(), nowTime.toLocalDate()) == 1) {
            result.append("Вчера");
        } else {
            result.append(rightWordPrinting(ChronoUnit.DAYS.between(givenTime.toLocalDate(), nowTime.toLocalDate()),
                    DAY_MODE));
        }
        return result.append("] ").toString();
    }
}
