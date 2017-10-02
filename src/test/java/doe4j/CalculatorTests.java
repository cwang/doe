package doe4j;

import org.junit.Test;

import java.security.MessageDigest;
import java.text.MessageFormat;

import static org.junit.Assert.assertEquals;

/**
 * Tests for Calculator.
 *
 * @see Calculator
 */
public class CalculatorTests {

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    @Test
    public void testOriginalConverters() {
        double deg = 90;
        double rad1 = deg2rad(deg);
        double rad2 = Math.toRadians(deg);
        System.out.println(rad1 + " | " + rad2);
        assertEquals(0, Double.compare(rad1, rad2));

        double rad = 1.57;
        double deg1 = rad2deg(rad);
        double deg2 = Math.toDegrees(rad);
        System.out.println(deg1 + " | " + deg2);
        assertEquals(0, Double.compare(deg1, deg2));
    }

    @Test
    public void testOtherDistanceMethods() {
        double dist1, dist2;

        dist1 = Calculator.distance(50.0664, 5.7147, 58.6439, 3.0070, "K");
        dist2 = Calculator.distance(50.0664, 5.7147, 58.6439, 3.0070, UnitOfLength.K);
        System.out.println(dist1 + " | " + dist2);
        assertEquals(0, Double.compare(Math.round(dist1 * 10), 9695));
        assertEquals(0, Double.compare(Math.round(dist2 * 10), 9695));

        dist1 = Calculator.distance("50 03 59 N", "005 42 53 W", "58 38 38 N", "003 04 12 W", "K");
        dist2 = Calculator.distance("50 03 59 N", "005 42 53 W", "58 38 38 N", "003 04 12 W", UnitOfLength.K);
        System.out.println(dist1 + " | " + dist2);
        assertEquals(0, Double.compare(Math.round(dist1 * 10), 9688));
        assertEquals(0, Double.compare(Math.round(dist2 * 10), 9688));
    }

    @Test
    public void testShortDistance() {
        UnitOfLength m = UnitOfLength.M;

        double dist = Calculator.distance(new PointOnEarth("51 30 0 N", "0 08 0 W"), new PointOnEarth("51 30 0 N", "0 00 0 W"), m);
        System.out.println("Distance Between London and Greenwich is " + m.format(dist) + " (" + dist + ")");
    }

    @Test
    public void testLongDistance() {
        UnitOfLength k = UnitOfLength.K;

        double dist = Calculator.distance(new PointOnEarth(51.50853, -0.12574), new PointOnEarth(-33.86785, 151.20732), k);
        System.out.println("Distance Between London and Sydney is " + k.format(dist) + " (" + dist + ")");
    }

    @Test
    public void testDistanceBetweenSpecialPoints() {
        double[] data = {0, 90, -90};
        UnitOfLength uol = UnitOfLength.M;
        String result = "between {0} and {1}: {2}";

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                PointOnEarth p1 = new PointOnEarth(data[i], data[j]);
                PointOnEarth p2 = new PointOnEarth(data[j], data[i]);
                double dist = Calculator.distance(p1, p2, uol);
                System.out.println(MessageFormat.format(result, p1, p2, uol.format(dist)));

            }
        }
    }
}
