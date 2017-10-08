package doe4j;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for PointOnEarth.
 *
 * @see doe4j.PointOnEarth
 */
public class PointOnEarthTests {

    String[] legalLats = {"0", "12.34", "-89.99", "90.00", "90 00 00 N", "0 0 0 S"};
    String[] legalLons = {"0", "12.34", "-89.99", "90.00", "90 00 00 W", "0 0 0 E"};
    String[] illegalLats = {"90.01", "-180", "12 34 56 W", "-1 2 S"};
    String[] illegalLons = {"180.01", "-200", "12 34 56 N", "-1 2 E"};
    String separator = ",";

    @Test
    public void testSignedDecimal() {

    }

    @Test
    public void testDegMinSecDir() {

    }

    @Test
    public void testCombinedPointRep() {
        for (String lat : legalLats) {
            for (String lon : legalLons) {
                String p = lat + separator + lon;
                PointOnEarth poe = PointOnEarth.parse(p);
                assertNotNull(poe);
                System.out.println(p + " => " + poe);
            }
            for (String ilon : illegalLons) {
                try {
                    String p = lat + separator + ilon;
                    PointOnEarth.parse(p);
                    fail("Expected exception on [" + p + "]!");
                }
                catch (IllegalArgumentException iae) {

                }
            }
        }

        for (String ilat : illegalLats) {
            for (String lon : legalLons) {
                try {
                    String p = ilat + separator + lon;
                    PointOnEarth.parse(p);
                    fail("Expected exception on [" + p + "]!");
                }
                catch (IllegalArgumentException iae) {

                }
            }
            for (String ilon : illegalLons) {
                try {
                    String p = ilat + separator + ilon;
                    PointOnEarth.parse(p);
                    fail("Expected exception on [" + p + "]!");
                }
                catch (IllegalArgumentException iae) {

                }
            }
        }
    }


}
