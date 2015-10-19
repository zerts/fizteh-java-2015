package ru.fizteh.fivt.students.zerts.moduletests.library;

import ru.fizteh.fivt.students.zerts.TwitterStream.exceptions.GeoExeption;
import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.JSONTokener;

import java.io.*;
import java.net.URL;
import java.util.Objects;

import static java.lang.Double.parseDouble;

public class GeoParser {
    static final int TIME_TO_WAIT_FOR_YANDEX = 100;
    public static String getKey() throws IOException, GeoExeption {
        try (BufferedReader in = new BufferedReader(new FileReader(
                GeoParser.class.getResource("/yandexkey.properties").getFile()))) {
            return in.readLine();
        } catch (IOException ioe) {
            throw new GeoExeption("Can't read the yandex key. Please, check your keyfile!");
        }
    }
    public static String getMyPlace() throws IOException, JSONException, GeoExeption {
        URL getCityName = new URL("http://api.hostip.info/get_json.php");
        String city;
        try (InputStream apihostipIn = getCityName.openStream()) {
            //String siteAnswer = apihostipIn.readLine();
            //System.out.println(apihostipIn);
            JSONTokener tokenizer = new JSONTokener(apihostipIn);
            JSONObject jsonParse = new JSONObject(tokenizer);
            city = jsonParse.getString("city");
            if (Objects.equals("(Unknown city)", city)) {
                getCityName = new URL("http://ipinfo.io/json");
                try (InputStream ipinfoIn = getCityName.openStream()) {
                    tokenizer = new JSONTokener(ipinfoIn);
                    jsonParse = new JSONObject(tokenizer);
                    city = jsonParse.getString("city");
                }
            }
        }
        if (Objects.equals(city, "")) {
            throw new GeoExeption("Can't detect your location");
        }
        //System.out.println(city);
        return city;
    }
    public static GeoLocation getCoordinates(String place) throws IOException, GeoExeption, InterruptedException,
            JSONException {
        if (place.equals("nearby")) {
            place = getMyPlace();
        }
        URL getTheLL = new URL("https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + place + "&apikey="
                + getKey());
        String city;
        System.out.println(getTheLL);
        try (InputStream yandexIn = getTheLL.openStream()) {
            //System.out.println(getTheLL);
            JSONTokener tokenizer = new JSONTokener(yandexIn);
            JSONObject jsonParse = new JSONObject(tokenizer);
            city = jsonParse.getJSONObject("response")
                    .getJSONObject("GeoObjectCollection")
                    .getJSONArray("featureMember")
                    .getJSONObject(0)
                    .getJSONObject("GeoObject")
                    .getJSONObject("Point")
                    .getString("pos");
        }
        String[] currLL= city.split(" ");
        //System.out.println(city);
        return new GeoLocation(parseDouble(currLL[1]), parseDouble(currLL[0]));
    }
    static double sqr(double number) {
        return number * number;
    }
    static final double EARTH_RADIUS = 6371;
    public static boolean near(GeoLocation first, GeoLocation second, double radius) {
        double firstLatitude = Math.toRadians(first.getLatitude());
        double firstLongtitute = Math.toRadians(first.getLongitude());
        double secondLatitude = Math.toRadians(second.getLatitude());
        double secondLongtitude = Math.toRadians(second.getLongitude());
        double deltaPhi = secondLatitude - firstLatitude;
        double deltaLambda = secondLongtitude - firstLongtitute;

        double distance = 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(firstLatitude) * Math.cos(secondLatitude) * sqr(Math.sin(deltaLambda / 2)))) * EARTH_RADIUS;
        //System.out.println(distance);
        return distance < radius;
    }
}

