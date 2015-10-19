package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import twitter4j.Status;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class TweetPrinter {
    static final int RT_MODE = 3;
    private static int printedTweets = 0;
    public static int getPrintedTweets() {
        return printedTweets;
    }
    public static String printTweet(Status tweet, ArgsParser argsPars, boolean streamMode) {
        String result = "";
        if (tweet.isRetweet() && argsPars.isNoRetweetMode()) {
            return result;
        }
        try {
            sleep(TimeUnit.SECONDS.toMillis(1L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!streamMode) {
            TimeParser timePars = new TimeParser();
            result += TimeParser.printGoneDate(tweet.getCreatedAt());
        }
        printedTweets++;
        result += "@" + tweet.getUser().getScreenName() + ": ";
        String text = tweet.getText();
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return "";
            }
            result += "ретвитнул @" + tweet.getRetweetedStatus().getUser().getScreenName()
                    + tweet.getRetweetedStatus().getText();
        } else {
            result += tweet.getText();
        }
        if (!argsPars.isStreamMode() && !tweet.isRetweet() && tweet.getRetweetCount() != 0) {
            result += " (" + TimeParser.rightWordPrinting(tweet.getRetweetCount(), RT_MODE) + ")";
        }
        result += Printer.printLine();
        return result;
    }
}
