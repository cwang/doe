package doe4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static doe4j.PointOnEarth.*;
import static doe4j.PointOnEarth.DegreeType.*;
import static org.junit.Assert.*;

/**
 * Tests for PointOnEarth.
 *
 * @see doe4j.PointOnEarth
 */
public class PointOnEarthTests {

    enum AssertType {
        LEGAL,
        ILLEGAL
    }

    enum DataType {
        SIGNED_DECIMAL,
        SPACED_DEG_MIN_SEC_DIR,
        SYMBOL_DEG_MIN_SEC_DIR
    }

    final Map<AssertType, Map<DataType, Map<PointOnEarth.DegreeType, String[]>>> data = new HashMap<>();

    final String separator = ",";

    @Before
    public void setUp() throws Exception {
        for (AssertType at : AssertType.values()) {
            data.put(at, new HashMap<>());

            for (DataType dt : DataType.values()) {
                data.get(at).put(dt, new HashMap<>());

                for (PointOnEarth.DegreeType gt : PointOnEarth.DegreeType.values()) {

                    String[] d = null;
                    switch (at) {
                        case LEGAL:
                            switch (dt) {
                                case SIGNED_DECIMAL:
                                    switch (gt) {
                                        case LATITUDE:
                                            d = new String[] {"0", "12.34", "-89.99", "90.00"};
                                            break;
                                        case LONGITUDE:
                                            d = new String[] {"0", "12.34", "-89.99", "90.00"};
                                            break;
                                    }
                                    break;
                                case SPACED_DEG_MIN_SEC_DIR:
                                    switch (gt) {
                                        case LATITUDE:
                                            d = new String[] {"90 00 00 N", "0 0 0 S"};
                                            break;
                                        case LONGITUDE:
                                            d = new String[] {"90 00 00 W", "0 0 0 E"};
                                            break;
                                    }
                                    break;
                                case SYMBOL_DEG_MIN_SEC_DIR:
                                    switch (gt) {
                                        case LATITUDE:
                                            d = new String[] {"90" + SYM_DEG + "00" + SYM_MIN + "00" + SYM_SEC + "N", "0" + SYM_DEG + "0" + SYM_MIN + "0" + SYM_SEC + "S"};
                                            break;
                                        case LONGITUDE:
                                            d = new String[] {"90" + SYM_DEG + "00" + SYM_MIN + "00" + SYM_SEC + "W", "0" + SYM_DEG + "0" + SYM_MIN + "0" + SYM_SEC + "E"};
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case ILLEGAL:
                            switch (dt) {
                                case SIGNED_DECIMAL:
                                    switch (gt) {
                                        case LATITUDE:
                                            d = new String[] {"90.01", "-180"};
                                            break;
                                        case LONGITUDE:
                                            d = new String[] {"180.01", "-200"};
                                            break;
                                    }
                                    break;
                                case SPACED_DEG_MIN_SEC_DIR:
                                    switch (gt) {
                                        case LATITUDE:
                                            d = new String[] {"12 34 56 W", "-1 2 S"};
                                            break;
                                        case LONGITUDE:
                                            d = new String[] {"12 34 56 N", "-1 2 E"};
                                            break;
                                    }
                                    break;
                                case SYMBOL_DEG_MIN_SEC_DIR:
                                    switch (gt) {
                                        case LATITUDE:
                                            d = new String[] {"12" + SYM_DEG + "34" + SYM_MIN + "56" + SYM_SEC + "W", "-1" + SYM_DEG + "2" + SYM_SEC + "S", "-3" + SYM_DEG + "4" + SYM_MIN + "S"};
                                            break;
                                        case LONGITUDE:
                                            d = new String[] {"12" + SYM_DEG + "34" + SYM_MIN + "56" + SYM_SEC + "N", "-1" + SYM_DEG + "2" + SYM_SEC + "E", "-3" + SYM_DEG + "4" + SYM_MIN + "E"};
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }

                    data.get(at).get(dt).put(gt, d);
                }
            }
        }

    }

    @After
    public void tearDown() throws Exception {
        data.clear();
    }

    private void doTestDataType(DataType dt) {
        for (AssertType at : AssertType.values()) {


            for (PointOnEarth.DegreeType gt : PointOnEarth.DegreeType.values()) {
                String[] testdata = data.get(at).get(dt).get(gt);

                for (String td : testdata) {
                    switch (at) {
                        case LEGAL:
                            double r = PointOnEarth.convert(td, gt);
                            assertFalse(Double.isNaN(r));
                            break;
                        case ILLEGAL:
                            try {
                                PointOnEarth.convert(td, gt);
                                fail("exception expected for [" + td + "] as " + gt);
                            }
                            catch (Exception e) {

                            }
                            break;
                    }
                }
            }
        }
    }

    @Test
    public void testSignedDecimal() {
        this.doTestDataType(DataType.SIGNED_DECIMAL);
    }

    @Test
    public void testSpacedDegMinSecDir() {
        this.doTestDataType(DataType.SPACED_DEG_MIN_SEC_DIR);
    }

    @Test
    public void testSymbolDegMinSecDir() {
        this.doTestDataType(DataType.SYMBOL_DEG_MIN_SEC_DIR);
    }

    @Test
    public void testCombinedPointRep() {

        List<String> legalLats = new LinkedList<>();
        List<String> legalLons = new LinkedList<>();

        List<String> illegalLats = new LinkedList<>();
        List<String> illegalLons = new LinkedList<>();

        for (AssertType at : AssertType.values()) {
            for (DataType dt : DataType.values()) {
                String[] lats = data.get(at).get(dt).get(LATITUDE);
                String[] lons = data.get(at).get(dt).get(LONGITUDE);

                switch (at) {
                    case LEGAL:
                        legalLats.addAll(Arrays.asList(lats));
                        legalLons.addAll(Arrays.asList(lons));
                        break;
                    case ILLEGAL:
                        illegalLats.addAll(Arrays.asList(lats));
                        illegalLons.addAll(Arrays.asList(lons));
                        break;
                }
            }
        }

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
