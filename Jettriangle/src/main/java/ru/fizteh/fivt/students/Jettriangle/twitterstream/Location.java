package ru.fizteh.fivt.students.Jettriangle.twitterstream;

/**
 * Created by rtriangle on 12.10.15.
 */
import twitter4j.GeoLocation;


public final class Location {
    private final double res;
    private final double latitudeCenter;
    private final double longitudeCenter;
    private final double latitudeSWCorner;
    private final double longitudeSWCorner;
    private final double latitudeNECorner;
    private final double longitudeNECorner;
    private final int error;

    public Location(GeoLocation center, GeoLocation cornerSW, GeoLocation cornerNE, double res) {
        this.latitudeCenter = center.getLatitude();
        this.longitudeCenter = center.getLongitude();
        this.latitudeSWCorner = cornerSW.getLatitude();
        this.longitudeSWCorner = cornerSW.getLongitude();
        this.latitudeNECorner = cornerNE.getLatitude();
        this.longitudeNECorner = cornerNE.getLongitude();
        this.res = res;
        this.error = 0;
    }

    public Location(int error) {
        this.latitudeCenter = 0;
        this.longitudeCenter = 0;
        this.latitudeSWCorner = 0;
        this.longitudeSWCorner = 0;
        this.latitudeNECorner = 0;
        this.longitudeNECorner = 0;
        this.res = 0;
        this.error = error;
    }

    public GeoLocation getGeoLocation() {
        return new GeoLocation(latitudeCenter, longitudeCenter);
    }

    public double getRes() {
        return res;
    }

    public double getLatitudeCenter() {
        return latitudeCenter;
    }

    public double getLongitudeCenter() {
        return longitudeCenter;
    }

    public double getLatitudeSWCorner() {
        return latitudeSWCorner;
    }

    public double getLongitudeSWCorner() {
        return longitudeSWCorner;
    }

    public double getLatitudeNECorner() {
        return latitudeNECorner;
    }

    public double getLongitudeNECorner() {
        return longitudeNECorner;
    }

    public int getError() {
        return error;
    }
}
