package nl.tudelft.context.controller.locator;

import de.saxsys.javafx.test.JfxRunner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nl.tudelft.context.controller.GraphController;
import nl.tudelft.context.drawable.graph.AbstractDrawableNode;
import nl.tudelft.context.drawable.graph.DrawableNode;
import nl.tudelft.context.model.graph.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author René Vennik
 * @version 1.0
 * @since 10-6-2015
 */
@RunWith(JfxRunner.class)
public class LocatorControllerTest {

    static LocatorController locatorController;
    static ObjectProperty<Map<Integer, List<AbstractDrawableNode>>> labelMapProperty = new SimpleObjectProperty<>();
    static ObjectProperty<List<Integer>> positionProperty = new SimpleObjectProperty<>(Collections.singletonList(1));

    /**
     * Set up the graph locator controller.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void beforeClass() throws Exception {
        labelMapProperty.set(new HashMap<>());

        locatorController = new LocatorController(
                new Pane(),
                labelMapProperty,
                mock(GraphController.class));
        locatorController.locatorIndicator = spy(new Rectangle());

        Map<Integer, List<AbstractDrawableNode>> map = new HashMap<>();
        map.put(0, Arrays.asList(
                getNode(0, 34),
                getNode(12, 23)
        ));
        map.put(1, Arrays.asList(
                getNode(42, 234),
                getNode(35, 344)
        ));
        map.put(2, Arrays.asList(
                getNode(200, 500),
                getNode(325, 800)
        ));
        labelMapProperty.set(map);
        locatorController.updatePosition(Arrays.asList(0, 1));

    }

    /**
     * Test that the location won't be updated if no label map present.
     */
    @Test
    public void testLocationNotUpdated() {

        LocatorController lc = new LocatorController(
                new Pane(),
                new SimpleObjectProperty<>(),
                mock(GraphController.class));

        lc.locatorIndicator = mock(Rectangle.class);
        lc.updatePosition(new ArrayList<>());
        verifyZeroInteractions(lc.locatorIndicator);

    }

    /**
     * Test if position is updated.
     * <p>
     * Not possible because of internal private methods.
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testPositionUpdate() throws Exception {

        assertEquals(0, locatorController.minRefPosition);
        assertEquals(800, locatorController.maxRefPosition);

    }

    /**
     * Get node to getBubblePostions with.
     *
     * @return Node
     */
    public static DrawableNode getNode(int refStart, int refEnd) {

        return new DrawableNode(new Node(0, new HashSet<>(), refStart, refEnd, "ATC"));

    }

}