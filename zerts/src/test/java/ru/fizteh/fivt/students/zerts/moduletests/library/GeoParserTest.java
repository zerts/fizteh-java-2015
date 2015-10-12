package ru.fizteh.fivt.students.zerts.moduletests.library;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import twitter4j.GeoLocation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@RunWith(PowerMockRunner.class)
public class GeoParserTest extends TestCase {
    private URL urlMockForIp;
    private URL urlMockForYandex;

    @Before
    public void setUp() throws Exception {
        urlMockForIp = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("http://api.hostip.info/get_json.php").thenReturn(urlMockForIp);
        urlMockForYandex = PowerMockito.mock(URL.class);
        PowerMockito.whenNew(URL.class).withArguments("https://geocode-maps.yandex.ru/1.x/?geocode="
                + "Moscow").thenReturn(urlMockForYandex);
    }

    @Test
    public void testGetMyPlace() throws Exception {
        String data = "{\"country_name\":\"RUSSIAN FEDERATION\",\"country_code\":"
                + "\"RU\",\"city\":\"Moscow\",\"ip\":\"77.37.136.173\"}";
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(urlMockForIp.openStream()).thenReturn(stream);

        String myPlace = GeoParser.getMyPlace();
        assertEquals("Moscow", myPlace);
    }

    @Test
    public void testGetCoordinates() throws Exception {
        String place = "Moscow";
        GeoLocation location = new GeoLocation(55.753960, 37.620393); //Moscow

        String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<ymaps xmlns=\"http://maps.yandex.ru/ymaps/1.x\" xmlns:xsi=\"" +
                "http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"" +
                "http://maps.yandex.ru/business/1.x http://maps.yandex.ru/schemas/business/1.x/business.xsd " +
                "http://maps.yandex.ru/geocoder/1.x http://maps.yandex.ru/schemas/geocoder/1.x/geocoder.xsd " +
                "http://maps.yandex.ru/psearch/1.x http://maps.yandex.ru/schemas/psearch/1.x/psearch.xsd " +
                "http://maps.yandex.ru/search/1.x http://maps.yandex.ru/schemas/search/1.x/search.xsd " +
                "http://maps.yandex.ru/web/1.x http://maps.yandex.ru/schemas/web/1.x/web.xsd " +
                "http://maps.yandex.ru/search/internal/1.x " +
                "http://maps.yandex.ru/schemas/search/internal/1.x/internal.xsd\">\n" +
                "<GeoObjectCollection>\n" +
                "<metaDataProperty xmlns=\"http://www.opengis.net/gml\">\n" +
                "<GeocoderResponseMetaData xmlns=\"http://maps.yandex.ru/geocoder/1.x\">\n" +
                "<request>Saint Peterburg</request>\n" +
                "<found>1</found>\n" +
                "<results>10</results>\n" +
                "</GeocoderResponseMetaData>\n" +
                "</metaDataProperty>\n" +
                "<featureMember xmlns=\"http://www.opengis.net/gml\">\n" +
                "<GeoObject xmlns=\"http://maps.yandex.ru/ymaps/1.x\" xmlns:gml=\"" +
                "http://www.opengis.net/gml\" gml:id=\"1\">\n" +
                "<metaDataProperty xmlns=\"http://www.opengis.net/gml\">\n" +
                "<GeocoderMetaData xmlns=\"http://maps.yandex.ru/geocoder/1.x\">\n" +
                "<kind>locality</kind>\n" +
                "<text>Р РѕСЃСЃРёСЏ, РЎР°РЅРєС‚-РџРµС‚РµСЂР±СѓСЂРі</text>\n" +
                "<precision>other</precision>\n" +
                "<AddressDetails xmlns=\"urn:oasis:names:tc:ciq:xsdschema:xAL:2.0\">\n" +
                "<Country>\n" +
                "<AddressLine>РЎР°РЅРєС‚-РџРµС‚РµСЂР±СѓСЂРі</AddressLine>\n" +
                "<CountryNameCode>RU</CountryNameCode>\n" +
                "<CountryName>Р РѕСЃСЃРёСЏ</CountryName>\n" +
                "<AdministrativeArea>\n" +
                "<AdministrativeAreaName>РЎРµРІРµСЂРѕ-Р—Р°РїР°РґРЅС‹Р№ " +
                "С„РµРґРµСЂР°Р»СЊРЅС‹Р№ РѕРєСЂСѓРі</AdministrativeAreaName>\n" +
                "<SubAdministrativeArea>\n" +
                "<SubAdministrativeAreaName>РЎР°РЅРєС‚-РџРµС‚РµСЂР±СѓСЂРі</SubAdministrativeAreaName>\n" +
                "<Locality>\n" +
                "<LocalityName>РЎР°РЅРєС‚-РџРµС‚РµСЂР±СѓСЂРі</LocalityName>\n" +
                "</Locality>\n" +
                "</SubAdministrativeArea>\n" +
                "</AdministrativeArea>\n" +
                "</Country>\n" +
                "</AddressDetails>\n" +
                "</GeocoderMetaData>\n" +
                "</metaDataProperty>\n" +
                "<description xmlns=\"http://www.opengis.net/gml\">Р РѕСЃСЃРёСЏ</description>\n" +
                "<name xmlns=\"http://www.opengis.net/gml\">РЎР°РЅРєС‚-РџРµС‚РµСЂР±СѓСЂРі</name>\n" +
                "<boundedBy xmlns=\"http://www.opengis.net/gml\">\n" +
                "<Envelope>\n" +
                "<lowerCorner>30.042834 59.744465</lowerCorner>\n" +
                "<upperCorner>30.568322 60.090935</upperCorner>\n" +
                "</Envelope>\n" +
                "</boundedBy>\n" +
                "<Point xmlns=\"http://www.opengis.net/gml\">\n" +
                "<pos>30.315868 59.939095</pos>\n";
        InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        PowerMockito.when(urlMockForYandex.openStream()).thenReturn(stream);

        assertEquals(location.getLatitude(), GeoParser.getCoordinates(place).getLatitude(), 1);
        assertEquals(location.getLongitude(), GeoParser.getCoordinates(place).getLongitude(), 1);
    }

    public void testNear() throws Exception {
        GeoLocation first = new GeoLocation(55.753960, 37.620393);
        GeoLocation second = new GeoLocation(56.753960, 39.620393);
        double radius = 5000;
        assertEquals(true, GeoParser.near(first, second, radius));

        first = new GeoLocation(55.753960, 37.620393);
        second = new GeoLocation(56.753960, 39.620393);
        radius = 1;
        assertEquals(false, GeoParser.near(first, second, radius));
    }
}
