package ru.fizteh.fivt.students.zerts.moduletests.library;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterStreamTest extends TestCase {
    private final String queryString = "a";
    private final String placeString = "Moscow";


    @Mock
    private GeoParser geoParser = new GeoParser(placeString);

    @Mock
    private Twitter twitter;

    @Mock
    private twitter4j.TwitterStream twitterStream;

    @InjectMocks
    private TwitterStream twitterService;

    public static List<Status> statuses = new ArrayList<Status>();

    @BeforeClass
    public static void loadSampleData() {
        statuses = Twitter4jTestUtils.tweetsFromJson("/twitterAnswer.json");
    }

    @Before
    public void setUp() throws Exception {
        ArgumentCaptor<StatusListener> statusCaptor
                = ArgumentCaptor.forClass(StatusListener.class);
        doNothing().when(twitterStream).addListener((StatusListener)
                statusCaptor.capture());
        doAnswer(invocation -> {
            statuses.forEach(s -> statusCaptor.getValue().onStatus(s));
            return null;
        }).when(twitterStream).filter(any(FilterQuery.class));

        GeoLocation moscowLocation = new GeoLocation(55.753960, 37.620393);
        when(geoParser.getCoordinates(placeString)).thenReturn(moscowLocation);
        when(geoParser.near(any(GeoLocation.class), any(GeoLocation.class), anyDouble())).thenReturn(true);
    }

    @Test
    public void testListenForTweets() throws Exception {

        List<String> tweets = new ArrayList<String>();

        ArgsParser argsParser = new ArgsParser(true, queryString, placeString, false);
        twitterService.listenForTweets(argsParser, tweets::add);

        assertThat(tweets, hasSize(100));
        assertThat(tweets, hasItems("@Owl_Juliann_: А мы сегодня с Викой добрались наконец-то до чайных дел "
                + "мастерской! На удивительной фарворовой… https://t.co/nenvrVtV0o"));
    }

    @Test
    public void testListenForTweets_no_RT() throws Exception {

        List<String> tweets = new ArrayList<String>();

        ArgsParser argsParser = new ArgsParser(true, queryString, placeString, true);
        twitterService.listenForTweets(argsParser, tweets::add);

        assertThat(tweets, hasSize(91));
    }
}
