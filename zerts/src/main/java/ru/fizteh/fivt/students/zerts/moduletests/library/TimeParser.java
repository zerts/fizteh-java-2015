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
        String result = String.valueOf(goneTime) + " ";
        if (goneTime % TEN_MOD >= FIVE || goneTime % TEN_MOD == 0 || (goneTime % HUNDRED_MOD > TEN_MOD
                && goneTime % HUNDRED_MOD < TWENTY)) {
            result += WORDS[mode][FIRST_FORM];
        } else if (goneTime % TEN_MOD == 1) {
            result += WORDS[mode][SECOND_FORM];
        } else {
            result += WORDS[mode][THIRD_FORM];
        }
        if (mode < RT_MODE) {
            result += " назад";
        }
        return result;
    }

    public static String printGoneDate(Date givenDate) {
        return printGoneDate(LocalDateTime.ofInstant(givenDate.toInstant(), ZoneId.systemDefault()),
                LocalDateTime.now());
    }

    public static String printGoneDate(LocalDateTime givenTime, LocalDateTime nowTime) {
        Duration goneTime = Duration.between(givenTime, nowTime);
        String result;
        if (goneTime.compareTo(JUST_NOW_TIME) < 0) {
            result = "Только что";
        } else if (goneTime.compareTo(Duration.ofHours(1)) < 0) {
            result = rightWordPrinting(goneTime.toMinutes(), MIN_MODE);
        } else if (givenTime.toLocalDate().equals(nowTime.toLocalDate())) {
            result = rightWordPrinting(goneTime.toHours(), HOUR_MODE);
        } else if (ChronoUnit.DAYS.between(givenTime.toLocalDate(), nowTime.toLocalDate()) == 1) {
            result = "Вчера";
        } else {
            result = rightWordPrinting(ChronoUnit.DAYS.between(givenTime.toLocalDate(), nowTime.toLocalDate()),
                    DAY_MODE);
        }
        return "[" + result + "] ";
    }
}
