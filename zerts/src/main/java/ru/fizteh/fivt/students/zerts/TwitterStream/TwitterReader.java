package ru.fizteh.fivt.students.zerts.TwitterStream;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.SearchTweetExeption;
import ru.fizteh.fivt.students.zerts.moduletests.library.ArgsParser;
import ru.fizteh.fivt.students.zerts.moduletests.library.TwitterQuery;
import ru.fizteh.fivt.students.zerts.moduletests.library.TwitterStream;
import twitter4j.JSONException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStreamFactory;

import java.io.IOException;
import java.util.List;

public class TwitterReader {
    static final int LOCATE_RADIUS = 50;
    public static int getLocateRadius() {
        return LOCATE_RADIUS;
    }
    public static void main(String[] args) throws IOException {
        ArgsParser argsPars = new ArgsParser();
        try {
            JCommander jComm = new JCommander(argsPars, args);
            if (argsPars.isHelpMode()) {
                jComm.usage();
            }
        } catch (ParameterException pe) {
            Printer.printError("Invalid Paramters:\n" + pe.getMessage());
        }
        twitter4j.Twitter twitter = new TwitterFactory().getInstance();
        twitter4j.TwitterStream twitter4jStream = new TwitterStreamFactory().getInstance();
        if (argsPars.isStreamMode()) {
            TwitterStream twitterStream = new TwitterStream(twitter, twitter4jStream);
            twitterStream.listenForTweets(argsPars, Printer::printTweet);
        } else {
            try {
                TwitterQuery twitterQuery = new TwitterQuery(twitter);
                List<String> queryResult;
                queryResult = twitterQuery.query(argsPars);
                if (queryResult == null || queryResult.isEmpty()) {
                    return;
                }
                for (String tweet : queryResult) {
                    Printer.print(tweet);
                }
            } catch (GeoExeption | SearchTweetExeption | InterruptedException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
