package nl.tudelft.context.model.annotation;

import nl.tudelft.context.model.annotation.Resistance;
import nl.tudelft.context.model.annotation.ResistanceMap;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Jasper on 26-5-2015.
 * @version 1.0
 * @since 26-5-2015
 */
public class ResistanceMapTest {


    protected static Resistance resistance1, resistance2;
    protected static ResistanceMap resistanceMap;

    /**
     * Set up by creating two annotations.
     */
    @BeforeClass
    public static void BeforeClass() {
        resistance1 = new Resistance("lorem", "ipsum", "Q264P", "dolor", 5, "set");
        resistance2 = new Resistance("set", "amet", "-11", "consecteur", 6, "adipiscing");
        resistanceMap = new ResistanceMap(Arrays.asList(resistance1, resistance2));
    }

    @Test
    public void testToString() throws Exception {
        // ???
    }

}