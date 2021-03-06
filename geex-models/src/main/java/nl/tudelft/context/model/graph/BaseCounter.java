package nl.tudelft.context.model.graph;

import org.apache.commons.collections.bag.HashBag;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Jasper Nieuwdorp
 * @version 1.2
 * @since 06-05-2015
 */
public class BaseCounter extends HashBag {

    /**
     * DecimalFormatter to format a float to 2 numbers after the period.
     */
    static DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

    /**
     * Constructor fot the empty base counter.
     */
    public BaseCounter() {
    }

    /**
     * Constructor for the base counter.
     *
     * @param bases String with the dna sequence
     */
    public BaseCounter(final String bases) {
        bases.chars()
                .mapToObj(x -> (char) x)
                .forEach(this::add);
    }

    /**
     * Get the percentage of a certain base, with N for an unknown base.
     *
     * @param c CATG char
     * @return float value with the percentage of the base in the initial string
     */
    public final float getRatio(final char c) {
        return (float) getCount(c) / size();
    }

    /**
     * Get a string representation of the percentage of a certain base, with n for an unknown base.
     *
     * @param c CATG char
     * @return string string representing the value with the percentage of the base in the initial string
     */
    public final String getPercentageString(final char c) {
        return df.format(getRatio(c) * 100);
    }

    /**
     * return a string representation of the percentages of all the bases in an node with a certain ID.
     *
     * @return string representation of all occurrence-rates of the bases in the BaseCounter.
     */
    @Override
    public final String toString() {
        return "A: "
                + getPercentageString('A')
                + "%, T: "
                + getPercentageString('T')
                + "%, C: "
                + getPercentageString('C')
                + "%, G: "
                + getPercentageString('G')
                + "%, N: "
                + getPercentageString('N')
                + "%";
    }

    /**
     * Add an other base counter to this base counter.
     *
     * @param baseCounter Base counter to add.
     */
    public void addBaseCounter(final BaseCounter baseCounter) {
        Arrays.asList('A', 'T', 'C', 'G', 'N').stream()
                .forEach(base -> add(base, baseCounter.getCount(base)));
    }
}
