package ru.fizteh.fivt.students.zerts.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import twitter4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.lang.Thread.*;

public class TwitterReader {
    private static int printedTweets = 0;
    static final int MILLS_PER_PER = 1000, LOCATE_RADIUS = 5, RT_MODE = 4;
    public static void printTweet(Status tweet, ArgsParser argsPars,
                                  boolean streamMode) {
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return;
            }
        }
        try {
            sleep(MILLS_PER_PER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!streamMode) {
            TimeParser timePars = new TimeParser();
            TimeParser.printGoneDate(tweet.getCreatedAt());
        }
        printedTweets++;
        System.out.print("@" + tweet.getUser().getScreenName() + ": ");
        int start = 0;
        String text = tweet.getText();
        if (tweet.isRetweet()) {
            if (argsPars.isNoRetweetMode()) {
                return;
            }
            System.out.print("ретвитнул ");
            while (text.charAt(start) != '@') {
                start++;
            }
        }
        for (int i = start; i < text.length(); i++) {
            if (text.charAt(i) != '\n') {
                System.out.print(text.charAt(i));
            } else {
                System.out.print(" ");
            }
        }
        if (!tweet.isRetweet()) {
            System.out.print(" (");
            TimeParser.rightWordPrinting(tweet.getRetweetCount(), RT_MODE);
            System.out.print(")");
        }
        System.out.print("\n\n");
        if (argsPars.getNumberOfTweets() == printedTweets) {
            System.exit(0);
        }
    }
    public static void main(String[] args) throws IOException {
        ArgsParser argsPars = new ArgsParser();
        try {
            JCommander jComm = new JCommander(argsPars, args);
            if (argsPars.isHelpMode()) {
                jComm.usage();
            }
        } catch (ParameterException pe) {
            System.err.print("Invalid Paramters:\n" + pe.getMessage());
            System.exit(-1);
        }
        //System.out.print(argsPars.place + "\n");
        if (argsPars.isHelpMode()) {
            BufferedReader in = new BufferedReader(
                    new FileReader("./zerts/src/main/java/ru/fizteh"
                            + "/fivt/students/zerts/TwitterStream/help.txt"));
            String currentLine = in.readLine();
            while (currentLine != null) {
                System.out.println(currentLine);
                currentLine = in.readLine();
            }
            System.exit(0);
        }
        Twitter twitter = new TwitterFactory().getInstance();
        if (argsPars.isStreamMode()) {
            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
                    try {
                        sleep(MILLS_PER_PER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                    System.err.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
                }
                @Override
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                    System.err.println("Got track limitation notice:" + numberOfLimitedStatuses);
                }
                @Override
                public void onScrubGeo(long userId, long upToStatusId) {
                    System.err.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
                }
                @Override
                public void onStallWarning(StallWarning warning) {
                    System.err.println("Got stall warning:" + warning);
                }
                @Override
                public void onException(Exception ex) {
                    ex.printStackTrace();
                }
            };
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.addListener(listener);
            String[] trackArray = argsPars.getQuery().toArray(
                    new String[argsPars.getQuery().size()]
            );
            twitterStream.filter(new FilterQuery().track(trackArray));

        } else if (argsPars.getQuery() == null) {
            try {
                int currPage = 1;
                User user = twitter.verifyCredentials();
                System.out.println("\nShowing @" + user.getScreenName()
                        + "'s home timeline.\n");
                do {
                    Paging p = new Paging(currPage);
                    List<Status> tweets = twitter.getHomeTimeline(p);
                    for (Status tweet : tweets) {
                        printTweet(tweet, argsPars, true);
                    }
                    currPage++;
                } while (true);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.err.println("Failed to get timeline: "
                        + te.getMessage());
                System.exit(-1);
            }
        } else {
            try {
                Query query;
                if (argsPars.getPlace() != null) {
                    query = new Query(
                            argsPars.getQuery().toString()).
                            geoCode(GeoParser.getCoordinates(
                                    argsPars.getPlace()), LOCATE_RADIUS, "km");
                } else {
                    query = new Query(argsPars.getQuery().toString());
                }
                QueryResult result;
                do {
                    result = twitter.search(query);
                    List<Status> tweets = result.getTweets();
                    System.out.print("Tweets with " + argsPars.getQuery());
                    if (argsPars.getPlace() != null) {
                        System.out.print(" near " + argsPars.getPlace());
                    }
                    System.out.print(":\n\n");
                    if (tweets.isEmpty()) {
                        System.out.println("Sorry, no tweets found :(");
                        System.exit(0);
                    }
                    for (Status tweet : tweets) {
                        printTweet(tweet, argsPars, false);
                    }
                    query = result.nextQuery();
                } while (query != null);
                System.exit(0);
            } catch (TwitterException te) {
                te.printStackTrace();
                System.err.println("Failed to search tweets: "
                        + te.getMessage());
                System.exit(-1);
            }
        }
    }
}