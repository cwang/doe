package doe;

/**
 * Cardinal direction, used in deg-min-sec representation of latitude/longitude measure.
 *
 * See https://en.wikipedia.org/wiki/Cardinal_direction for details.
 */
public enum CardinalDirection {
    N(1.0d, "North"),
    E(1.0d, "East"),
    S(-1.0d, "South"),
    W(-1.0d, "West");

    private double sign;
    private String label;

    CardinalDirection(double sign, String label) {
        this.sign = sign;
        this.label = label;
    }

    public double getSign() {
        return sign;
    }

    public String getLabel() {
        return label;
    }
}
