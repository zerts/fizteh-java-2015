package ru.fizteh.fivt.students.zerts.moduletests.library;
import com.beust.jcommander.Parameter;

public class ArgsParser {
    static final int NUMBER_OF_TWEETS_WITHOUT_LIMIT = 100;
    @Parameter(names = {"-s", "--stream"}, description = "stream output")
    private boolean streamMode;
    @Parameter(names = {"-q", "--query"}, description = "query output")
    private String query;
    @Parameter(names = {"-p", "--place"}, description = "place output")
    private String place = "nearby";
    @Parameter(names = "--hideRetweets", description = "no retweets output")
    private boolean noRetweetMode = false;
    @Parameter(names = {"-l", "--limit"}, description = "limited output")
    private int numberOfTweets = NUMBER_OF_TWEETS_WITHOUT_LIMIT;
    @Parameter(names = {"-h", "--help"}, description = "help output")
    private boolean helpMode;

    public final boolean isStreamMode() {
        return streamMode;
    }
    public final String getQuery() {
        return query;
    }
    public final String getPlace() {
        return place;
    }
    public final boolean isNoRetweetMode() {
        return noRetweetMode;
    }
    public final int getNumberOfTweets() {
        return numberOfTweets;
    }
    public final boolean isHelpMode() {
        return helpMode;
    }

    public ArgsParser() {

    }
    public ArgsParser(boolean streamMode, String query, String place, boolean noRetweetMode) {
        this.streamMode = streamMode;
        this.query = query;
        this.place = place;
        this.noRetweetMode = noRetweetMode;
    }
    public ArgsParser(boolean streamMode, String query, String place, boolean noRetweetMode, int numberOfTweets) {
        this.streamMode = streamMode;
        this.query = query;
        this.place = place;
        this.noRetweetMode = noRetweetMode;
        this.numberOfTweets = numberOfTweets;
    }
}
