package doe4j;

import java.text.MessageFormat;

/**
 * Unit of length, used to specify calculated end result for distance.
 *
 * See https://en.wikipedia.org/wiki/Unit_of_length for details.
 */
public enum UnitOfLength {
    M(1.0d, "Miles"),
    K(1.609344d, "Kilometres"),
    N(0.8684d, "Nautical Miles");

    private static final String FORMAT_DISTANCE = "{0,number,#.###} {1}";

    private double ratio;
    private String label;

    UnitOfLength(double ratio, String label) {
        this.ratio = ratio;
        this.label = label;
    }

    public double getRatio() {
        return ratio;
    }

    public String getLabel() {
        return label;
    }

    public String format(double distance) {
        return MessageFormat.format(FORMAT_DISTANCE, distance, this.getLabel());
    }
}
