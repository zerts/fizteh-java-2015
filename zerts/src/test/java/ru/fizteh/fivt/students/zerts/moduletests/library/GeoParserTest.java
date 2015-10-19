package ru.fizteh.fivt.students.zerts.moduletests.library;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import twitter4j.GeoLocation;
import twitter4j.Location;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GeoParser.class)
public class GeoParserTest extends TestCase {
    private URL getCityName;
    private URL getTheLL;
    static final String PLACE = "Moscow";
    GeoParser geoParser = new GeoParser("nearby");

    @Before
    public void setUp() throws Exception {
        getCityName = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("http://api.hostip.info/get_json.php").thenReturn(getCityName);

        getTheLL = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("https://geocode-maps.yandex.ru/1.x/?format=json&geocode="
                + PLACE + "&apikey=" + GeoParser.getKey()).thenReturn(getTheLL);
    }

    @Test
    public void testGetMyPlace() throws Exception {
        String data = "{\"country_name\":\"RUSSIAN FEDERATION\",\"country_code\":"
                + "\"RU\",\"city\":\"" + PLACE + "\",\"ip\":\"77.37.136.173\"}";
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(getCityName.openStream()).thenReturn(stream);

        String myPlace = geoParser.getMyPlace();
        System.out.println(myPlace);
        assertEquals(PLACE, myPlace);
    }

    @Test
    public void testGetCoordinates() throws Exception {
        GeoLocation location = new GeoLocation(55.753960, 37.620393); //Moscow
        String data;
        try (BufferedReader in = new BufferedReader(new FileReader(
                GeoParser.class.getResource("/yandexAnswer").getFile()))) {
            data = in.readLine();
        }
        //System.out.println(data);
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(getTheLL.openStream()).thenReturn(stream);
        //System.out.println(getTheLL);
        GeoLocation geoParserResult = geoParser.getCoordinates(PLACE);
        assertEquals(location.getLatitude(), geoParserResult.getLatitude(), 1);
        assertEquals(location.getLongitude(), geoParserResult.getLongitude(), 1);
    }

    public void testNear() throws Exception {
        GeoLocation first = new GeoLocation(55.753960, 37.620393);
        GeoLocation second = new GeoLocation(56.753960, 39.620393);
        double radius = 5000;
        assertEquals(true, geoParser.near(first, second, radius));

        first = new GeoLocation(55.753960, 37.620393);
        second = new GeoLocation(56.753960, 39.620393);
        radius = 1;
        assertEquals(false, geoParser.near(first, second, radius));
    }
}
