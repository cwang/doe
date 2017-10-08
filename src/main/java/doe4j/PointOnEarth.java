package doe4j;

import com.sun.javafx.geom.transform.BaseTransform;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
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

    public static final String FORMAT_SIGNED_DECIMAL_DEGREE = "{0,number}{1}";

    private static final String[] VARS_IN_DEG_MIN_SEC_DIR = {"{1,number}", "{2,number}", "{3,number}", "{0}"};
    public static final String FORMAT_SPACED_DEG_MIN_SEC_DIR = String.join(" ", VARS_IN_DEG_MIN_SEC_DIR);
    public static final String FORMAT_SYMBOL_DEG_MIN_SEC_DIR =
            VARS_IN_DEG_MIN_SEC_DIR[0] + "\u00B0" + VARS_IN_DEG_MIN_SEC_DIR[1] + "\u2032" + VARS_IN_DEG_MIN_SEC_DIR[2] + "\u2033" + VARS_IN_DEG_MIN_SEC_DIR[3];

    public static final String FORMAT_LAT_LON = "{0},{1}";

    private enum DegreeType {
        LATITUDE(CardinalDirection.LATITUDE, 90d, -90d),
        LONGITUDE(CardinalDirection.LONGITUDE, 180d, -180d);

        private CardinalDirection[] directions;
        private double upperBound;
        private double lowerBound;

        DegreeType(CardinalDirection[] directions, double upperBound, double lowerBound) {
            this.directions = directions;
            this.upperBound = upperBound;
            this.lowerBound = lowerBound;
        }

        public CardinalDirection[] getDirections() {
            return directions;
        }

        public double getUpperBound() {
            return upperBound;
        }

        public double getLowerBound() {
            return lowerBound;
        }

        public boolean bounds(double deg, boolean nonNegative) {
            return nonNegative ? (deg <= this.getUpperBound() && deg >= 0) : (deg <= this.getUpperBound() && deg >= this.getLowerBound());
        }

        public boolean accepts(CardinalDirection dir) {
            return Arrays.asList(this.getDirections()).contains(dir);
        }
    }

    private interface DegreeParser {
        double parse(Object[] data, DegreeType type);
    }

    public PointOnEarth(String lat, String lon) {
        this.latitude = this.convert(lat, DegreeType.LATITUDE);
        this.longitude = this.convert(lon, DegreeType.LONGITUDE);
    }

    public PointOnEarth(double latitude, double longitude) {
        this(String.valueOf(latitude), String.valueOf(longitude));
    }

    public static PointOnEarth parse(String latlon) {
        try {
            Object[] data = new MessageFormat(FORMAT_LAT_LON).parse(latlon);
            if (data.length == 2) {
                return new PointOnEarth((String) data[0], (String) data[1]);
            }
            else {
                throw new IllegalArgumentException("The supplied combined lat/lon [" + latlon + "] data has been parsed using format [" + FORMAT_LAT_LON + "] but is invalid as [" + Arrays.asList(data) + "]!");
            }
        }
        catch (IllegalArgumentException iae) {
            throw iae;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("The supplied combined lat/lon [" + latlon + "] data cannot be parsed using format [" + FORMAT_LAT_LON + "]!", e);
        }
    }

    public static double convert(String degree, DegreeType type) {
        double result = Double.NaN;

        result = tryParse(result, degree, type, FORMAT_SYMBOL_DEG_MIN_SEC_DIR, new DegreeParser() {
            @Override
            public double parse(Object[] data, DegreeType type) {
                validateDegMinSecDir(data, type);

                return CardinalDirection.valueOf((String) data[0]).getSign()
                        * ((Long) data[1] + ((Long) data[2] * 60d + (Long) data[3]) / (60 * 60));

            }
        });

        result = tryParse(result, degree, type, FORMAT_SPACED_DEG_MIN_SEC_DIR, new DegreeParser() {
            @Override
            public double parse(Object[] data, DegreeType type) {
                validateDegMinSecDir(data, type);

                return CardinalDirection.valueOf((String) data[0]).getSign()
                        * ((Long) data[1] + ((Long) data[2] * 60d + (Long) data[3]) / (60 * 60));
            }
        });

        result = tryParse(result, degree, type, FORMAT_SIGNED_DECIMAL_DEGREE, new DegreeParser() {
            @Override
            public double parse(Object[] data, DegreeType type) {
                validateDecimalDegree(data, type);
                return new BigDecimal(data[0].toString()).doubleValue();
            }
        });

        if (Double.isNaN(result)) {
            throw new IllegalArgumentException("The supplied lat/lon [" + degree + "] data cannot be converted according to either expected format!");
        }
        return result;
    }

    private static double tryParse(double result, String degree, DegreeType type, String format, DegreeParser parser) {

        if (Double.isNaN(result)) {
            try {

                Object[] data = new MessageFormat(format).parse(degree);

                return parser.parse(data, type);
            }
            catch(ParseException pe){
                // deferred to later handling.
                return Double.NaN;
            }
        }
        else {
            return result;
        }
    }

    private static void validateDegMinSecDir(Object[] data, DegreeType type) {
        if (data.length == 4 && data[0] instanceof String
                && (data[1] instanceof Long || data[1] instanceof Double)
                && (data[2] instanceof Long || data[2] instanceof Double)
                && (data[3] instanceof Long || data[3] instanceof Double)) {

            CardinalDirection dir = CardinalDirection.valueOf((String) data[0]);
            if (! type.accepts(dir)) {
                throw new IllegalArgumentException("The lat/lon supplied [" + Arrays.asList(data) + "] has wrong direction!");
            }

            for (int i = 1; i <= 3; i++) {
                double deg = new BigDecimal(data[i].toString()).doubleValue();
                if (! type.bounds(deg, true)) {
                    throw new IllegalArgumentException("The lat/lon supplied [" + Arrays.asList(data) + "] is out of bound!");
                }
            }
        }
        else {
            throw new IllegalArgumentException("The lat/lon supplied [" + Arrays.asList(data) + "] is malformed!");
        }
    }

    private static void validateDecimalDegree(Object[] data, DegreeType type) {
        if (data.length == 2 && ((String) data[1]).trim().isEmpty()
                && (data[0] instanceof Double || data[0] instanceof Long)) {

            double deg = new BigDecimal(data[0].toString()).doubleValue();
            if (! type.bounds(deg, false)) {
                throw new IllegalArgumentException("The lat/lon supplied [" + Arrays.asList(data) + "] is out of bound!");
            }
        }
        else {
            throw new IllegalArgumentException("The lat/lon supplied [" + Arrays.asList(data) + "] is malformed!");
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return MessageFormat.format(FORMAT_LAT_LON,
                MessageFormat.format(FORMAT_SIGNED_DECIMAL_DEGREE, this.latitude, ""),
                MessageFormat.format(FORMAT_SIGNED_DECIMAL_DEGREE, this.longitude, ""));
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
