package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import twitter4j.Status;

public class TweetPrinter {
    static final int RT_MODE = 3;
    public static void setPrintedTweets(int printedTweets) {
        TweetPrinter.printedTweets = printedTweets;
    }
    private static int printedTweets = 0;
    public static int getPrintedTweets() {
        return printedTweets;
    }
    public static String printTweet(Status tweet, ArgsParser argsPars, boolean streamMode) {
        StringBuilder result = new StringBuilder();
        if (tweet.isRetweet() && argsPars.isNoRetweetMode()) {
            return null;
        }
        if (!streamMode) {
            TimeParser timePars = new TimeParser();
            result.append(TimeParser.printGoneDate(tweet.getCreatedAt()));
        }
        printedTweets++;
        result.append("@").append(tweet.getUser().getScreenName()).append(": ");
        String text = tweet.getText();
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return "";
            }
            result.append("ретвитнул @").append(tweet.getRetweetedStatus().getUser().getScreenName())
                    .append(tweet.getRetweetedStatus().getText());
        } else {
            result.append(tweet.getText());
        }
        if (!argsPars.isStreamMode() && !tweet.isRetweet() && tweet.getRetweetCount() != 0) {
            result.append(" (").append(TimeParser.rightWordPrinting(tweet.getRetweetCount(), RT_MODE)).append(")");
        }
        result.append(Printer.printLine());
        return result.toString();
    }
}
