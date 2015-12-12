package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.Printer;
import ru.fizteh.fivt.students.zerts.TwitterStream.TwitterReader;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import twitter4j.*;

import java.io.IOException;
import java.util.function.Consumer;

public class TwitterStream {

    private final twitter4j.TwitterStream twitterStream;

    private GeoParser geoParser = new GeoParser("Moscow");

    public TwitterStream(Twitter twitter, twitter4j.TwitterStream twitterStream) {
        this.twitterStream = twitterStream;
    }

    private String formatTweet(Status status) {
        /*try {
            sleep(TimeUnit.SECONDS.toMillis(1L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "@" + status.getUser().getScreenName() + ": " + status.getText();
    }

    public void listenForTweets(ArgsParser argsPars, Consumer<String> listener) {
        twitterStream.addListener(new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                if (argsPars.getPlace() == null) {
                    Printer.print(TweetPrinter.printTweet(status, argsPars, true));
                } else {
                    GeoLocation tweetLocation = null;
                    if (status.getGeoLocation() != null) {
                        tweetLocation = new GeoLocation(status.getGeoLocation().getLatitude(),
                                status.getGeoLocation().getLongitude());
                    } else if (!(status.getUser().getLocation() == null)) {
                        try {
                            //System.out.println(status.getUser().getLocation());
                            try {
                                tweetLocation = geoParser.getCoordinates(status.getUser().getLocation());
                                //System.out.println(tweetLocation);
                            } catch (GeoExeption | InterruptedException | JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        return;
                    }
                    try {
                        GeoLocation queryLocation = null;
                        try {
                            queryLocation = geoParser.getCoordinates(argsPars.getPlace());
                        } catch (GeoExeption | InterruptedException | JSONException e) {
                            e.printStackTrace();
                        }
                        if (geoParser.near(tweetLocation, queryLocation, TwitterReader.getLocateRadius())
                                && TweetPrinter.printTweet(status, argsPars, true) != null) {
                            Printer.print(TweetPrinter.printTweet(status, argsPars, true));
                            listener.accept(formatTweet(status));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        });
        String[] query = new String[1];
        query[0] = argsPars.getQuery();
        twitterStream.filter(new FilterQuery().track(query));
    }
}
