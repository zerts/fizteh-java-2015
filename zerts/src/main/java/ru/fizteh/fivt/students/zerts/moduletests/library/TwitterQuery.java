package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import ru.fizteh.fivt.students.zerts.TwitterStream.TwitterReader;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.SearchTweetExeption;
import twitter4j.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterQuery {
    private twitter4j.Twitter twitter;

    public TwitterQuery(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<String> query(ArgsParser argsPars) throws IOException, GeoExeption, SearchTweetExeption,
            InterruptedException, JSONException {
        TweetPrinter.setPrintedTweets(0);
        GeoParser geoParser = new GeoParser(argsPars.getPlace());
        List<String> resultTweets = new ArrayList<>();
        try {
            GeoLocation queryLocation = geoParser.getCoordinates(argsPars.getPlace());
            if (queryLocation == null) {
                throw new GeoExeption("Bad query");
            }
            if (argsPars.getQuery() == null) {
                throw new SearchTweetExeption("Null query");
            }
            Query query = new Query(argsPars.getQuery()).geoCode(queryLocation,
                    TwitterReader.getLocateRadius(), "km");
            //System.out.println(argsPars.getQuery().toString());
            query.setCount(argsPars.getNumberOfTweets());
            QueryResult result;
            //System.out.println(query);
            result = twitter.search(query);
            //System.out.println(result);
            List<Status> tweets = result.getTweets();
            Printer.print("Tweets with " + argsPars.getQuery() + " near ");
            if (argsPars.getPlace() != null) {
                if (argsPars.getPlace().equals("nearby")) {
                    Printer.print(geoParser.getMyPlace());
                } else {
                    Printer.print(argsPars.getPlace());
                }
            }
            Printer.print(Printer.printLine());
            if (tweets.isEmpty()) {
                Printer.print("Sorry, no tweets found :(\n");
                return resultTweets;
            }
            for (Status tweet : tweets) {
                String newTweet = TweetPrinter.printTweet(tweet, argsPars, false);
                if (newTweet != null && newTweet.length() > 1) {
                    resultTweets.add(newTweet);
                }
                //System.out.println(argsPars.getNumberOfTweets() + ' ' + TweetPrinter.getPrintedTweets());
                if (argsPars.getNumberOfTweets() == TweetPrinter.getPrintedTweets()) {
                    return resultTweets;
                }
            }
        } catch (TwitterException te) {
            throw new SearchTweetExeption();
        }
        return resultTweets;
    }
}
