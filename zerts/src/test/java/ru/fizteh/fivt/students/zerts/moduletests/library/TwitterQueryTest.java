package ru.fizteh.fivt.students.zerts.moduletests.library;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.SearchTweetExeption;
import twitter4j.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterQueryTest extends TestCase {
    private final GeoLocation moscowLocation = new GeoLocation(55.7500, 37.6167);
    private String queryString = "a";

    @Mock
    QueryResult queryResult;

    @Mock
    private Twitter twitter;

    @Mock
    private GeoParser geoParser = new GeoParser("Moscow");

    /**
     * Mockito calls <pre>new TwitterServiceImpl(twitter, twitterStream)</pre> here
     */
    @InjectMocks
    private TwitterQuery twitterQuery;

    public static List<Status> statuses;

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/twitterAnswer.json");
    }

    /**
     * Preparing sample data for test. Initializing mocks
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        when(geoParser.getCoordinates("Moscow")).thenReturn(moscowLocation);
        queryResult = mock(QueryResult.class);
        when(queryResult.getTweets()).thenReturn(statuses);

        QueryResult emptyQueryResult = mock(QueryResult.class);
        when(emptyQueryResult.getTweets()).thenReturn(Collections.emptyList());
        when(twitter.search(argThat(hasProperty("query", equalTo(queryString))))).thenReturn(queryResult);

        when(twitter.search(argThat(hasProperty("query", not(equalTo(queryString)))))).thenReturn(emptyQueryResult);
    }

    @Test
    public void testGetFormattedTweets() throws Exception {
        ArgsParser argsParser = new ArgsParser(true, queryString, "Moscow", false);
        List<String> tweets = twitterQuery.query(argsParser);
        PowerMockito.when(twitter.search(argThat(hasProperty("query", equalTo(queryString))))).thenReturn(queryResult);


        //System.out.println(tweets.size());
        assertThat(tweets.size(), is(100));
        long daysGone = 9 + ChronoUnit.DAYS.between(LocalDateTime.of(2015, Month.OCTOBER, 30, 19, 20).toLocalDate(),
                LocalDateTime.now().toLocalDate());
        StringBuilder result = new StringBuilder().append(" ");
        int TEN_MOD = 10;
        int FIVE = 5;
        int HUNDRED_MOD = 100;
        int TWENTY = 20;
        if (daysGone % TEN_MOD >= FIVE || daysGone % TEN_MOD == 0 || (daysGone % HUNDRED_MOD > TEN_MOD
                && daysGone % HUNDRED_MOD < TWENTY)) {
            result.append("дней");
        } else if (daysGone % TEN_MOD == 1) {
            result.append("день");
        } else {
            result.append("дня");
        }
        assertThat(tweets, hasItems("[" + daysGone + result.toString() + " назад] @Owl_Juliann_: We are here! "
                + "https://t.co/nenvrVtV0o\n---------------------------------------------------"
                + "----------------------------------------------------------------------------"
                + "-------------------------------------------\n"));

    }

    @Test
    public void testGetFormattedTweets_no_RT() throws Exception {
        ArgsParser argsParser = new ArgsParser(true, queryString, "Moscow", true);
        List<String> tweets = twitterQuery.query(argsParser);
        PowerMockito.when(twitter.search(argThat(hasProperty("query", equalTo(queryString))))).thenReturn(queryResult);

        assertThat(tweets.size(), is(91));
    }

    @Test
    public void testGetFormattedTweets_limited() throws Exception {
        ArgsParser argsParser = new ArgsParser(true, queryString, "Moscow", false, 23);
        List<String> tweets = twitterQuery.query(argsParser);
        PowerMockito.when(twitter.search(argThat(hasProperty("query", equalTo(queryString))))).thenReturn(queryResult);

        assertThat(tweets.size(), is(23));
    }

    @Test
    public void testGetFormattedTweets_empty_result() throws Exception {
        ArgsParser argsParser = new ArgsParser(true, "c#", "Moscow", true);
        List<String> tweets = twitterQuery.query(argsParser);
        assertThat(tweets, hasSize(0));

        verify(twitter).search(argThat(hasProperty("query", equalTo("c#"))));
    }

    @Test(expected = SearchTweetExeption.class)
    public void testGetFormattedTweets_null_query() throws Exception {
        ArgsParser argsParser = new ArgsParser(true, null, "Moscow", true);
        twitterQuery.query(argsParser);
    }
}
