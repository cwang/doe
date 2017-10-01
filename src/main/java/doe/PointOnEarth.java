package doe;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Objects;

/**
 * A latitude-longitude representation of a point on Earth.
 *
 * There are two constructors, taking signed decimal degrees and deg-min-sec-dir formatted strings respectively.
 *
 * See https://en.wikipedia.org/wiki/Geographic_coordinate_system#Geographic.28al.29_latitude_and_longitude for details.
 */
public class PointOnEarth implements Serializable {

    private double latitude;
    private double longitude;


    public static final String FORMAT_SIGNED_DECIMAL_DEGREE = "{0,number,0.#####}";
    public static final String FORMAT_DEG_MIN_SEC_DIR = "{1,number,integer} {2,number,integer} {3,number,integer} {0}";
    public static final String SEPARATOR = ",";

    public PointOnEarth(double latitude, double longitude) {
        this.init(latitude, longitude);
    }

    public PointOnEarth(String lat, String lon) {
        this.init(this.convert(lat), this.convert(lon));
    }

    private double convert(String latlon) {
        double result;
        try {
            Object[] data = new MessageFormat(FORMAT_DEG_MIN_SEC_DIR).parse(latlon);

            result = CardinalDirection.valueOf((String) data[0]).getSign()
                    * ((Long) data[1] + ((Long) data[2] * 60d + (Long) data[3]) / (60 * 60));
        }
        catch (Exception e) {
            throw new IllegalArgumentException("The supplied lat/lon [" + latlon + "] data cannot be parsed according to expected format [" + FORMAT_DEG_MIN_SEC_DIR + "]!", e);
        }
        return result;
    }

    private void init(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return MessageFormat.format(FORMAT_SIGNED_DECIMAL_DEGREE, this.latitude)
                + SEPARATOR + MessageFormat.format(FORMAT_SIGNED_DECIMAL_DEGREE, this.longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointOnEarth that = (PointOnEarth) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
