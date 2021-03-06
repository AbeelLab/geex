package nl.tudelft.context.model.newick.node;

import nl.tudelft.context.model.newick.selection.All;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Jasper Boot
 * @version 1.0
 * @since 10-06-2015
 */
public class AncestorNodeTest {
    AncestorNode parent = new AncestorNode(1.23);
    AncestorNode child1 = new AncestorNode(2.34);
    AncestorNode child2 = new AncestorNode(3.45);

    /**
     * Set up basic structure.
     */
    @Before
    public void before() {
        connect(
                parent, child1,
                parent, child2
        );
    }

    /**
     * Test the name.
     */
    @Test
    public void testName() {
        assertEquals("", parent.getName());
    }

    /**
     * Test the weight.
     */
    @Test
    public void testWeight() {
        assertEquals(1.23, parent.getWeight(), 1e-12);
    }

    /**
     * Parent should contain all children.
     */
    @Test
    public void testChildren() {
        assertEquals(child1, parent.getChildren().get(0));
        assertEquals(child2, parent.getChildren().get(1));
        assertTrue(child1.getChildren().isEmpty());
        assertTrue(child2.getChildren().isEmpty());
    }

    /**
     * None selected; no sources.
     */
    @Test
    public void testUpdateSources() {
        parent.updateSources();
        assertTrue(parent.getSources().isEmpty());
    }

    /**
     * One source should be returned.
     */
    @Test
    public void testUpdateSourcesNotEmpty() {
        AbstractNode node = mock(AbstractNode.class);
        Set<String> source = new HashSet<>();
        source.add("cheese");
        when(node.getSources()).thenReturn(source);

        child1.addChild(node);
        child1.updateSources();
        assertEquals(1, child1.getSources().size());
        assertTrue(child1.getSources().contains("cheese"));
    }

    /**
     * A new node should be returned with the same name and weight.
     */
    @Test
    public void testCopy() {
        AbstractNode node = parent.getCopy();
        assertEquals(node.getName(), parent.getName());
        assertEquals(node.getWeight(), parent.getWeight(), 1e-12);
        assertFalse(node == parent);
    }

    /**
     * Should return all selected nodes.
     */
    @Test
    public void testGetSelected() {
        parent.setSelection(new All());
        AbstractNode root = parent.getSelectedNodes();
        assertEquals(parent.getWeight(), root.getWeight(), 1e-12);
        assertEquals(2, root.getChildren().size());
    }

    /**
     * Should return all selected nodes, minus ancestors with only one child.
     */
    @Test
    public void testGetSelectedSkipAncestor() {
        parent.setSelection(new All());
        child1.toggleSelection();
        AbstractNode root = parent.getSelectedNodes();
        assertEquals(child2.getWeight(), root.getWeight(), 1e-12);
        assertTrue(root.getChildren().isEmpty());
    }

    /**
     * The right class name should be returned.
     */
    @Test
    public void testClassName() {
        assertEquals("newick-ancestor", parent.getClassName());
    }

    /**
     * Translate x, on top of the parent's location.
     */
    @Test
    public void testTranslation() {
        int minWeight = 3;
        double weightScale = 11;
        int yPos = 7;
        child1.translate(minWeight, weightScale, yPos);
        assertEquals(
                minWeight + child1.getWeight() * weightScale,
                child1.translateXProperty().doubleValue(), 1e-12);
        assertEquals(yPos, child1.translateYProperty().intValue());
        parent.setTranslateX(3);
        child1.translate(minWeight, weightScale, yPos);
        assertEquals(
                minWeight + child1.getWeight() * weightScale + 3,
                child1.translateXProperty().doubleValue(), 1e-12);
    }

    /**
     * Test toString().
     */
    @Test
    public void testToString() {
        assertEquals("AncestorNode<1.23>", parent.toString());
    }

    /**
     * Connects every two nodes.
     *
     * @param nodes The nodes to connect
     */
    private void connect(AbstractNode... nodes) {
        for (int i = 0; i < nodes.length; i += 2) {
            connect(nodes[i], nodes[i + 1]);
        }
    }

    /**
     * Connects the parent to the child and visa versa.
     *
     * @param parent The parent node
     * @param child  The child node
     */
    private void connect(AbstractNode parent, AbstractNode child) {
        parent.addChild(child);
        child.setParent(parent);
    }
}