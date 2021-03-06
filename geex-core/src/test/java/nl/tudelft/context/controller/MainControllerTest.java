package nl.tudelft.context.controller;

import de.saxsys.javafx.test.JfxRunner;
import nl.tudelft.context.model.graph.BaseCounter;
import nl.tudelft.context.model.graph.Graph;
import nl.tudelft.context.model.graph.GraphParser;
import nl.tudelft.context.model.graph.Node;
import nl.tudelft.context.workspace.Workspace;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author René Vennik
 * @version 1.0
 * @since 26-4-2015
 */
@RunWith(JfxRunner.class)
public class MainControllerTest {

    protected static Graph graph;

    protected static final Node node1 = new Node(0, new HashSet<>(Arrays.asList("Cat", "Dog")), 5, 7, "A");
    protected static final Node node2 = new Node(1, new HashSet<>(Collections.singletonList("Dog")), 7, 10, "C");

    protected static MainController mainController;
    protected static Workspace workspace;

    /**
     * Setup Main Controller.
     */
    @BeforeClass
    public static void beforeClass() throws Exception {

        File nodeFile = new File(MainControllerTest.class.getResource("/graph/node.graph").getPath());
        File edgeFile = new File(MainControllerTest.class.getResource("/graph/edge.graph").getPath());

        graph = new GraphParser().setFiles(nodeFile, edgeFile).load().flat(new HashSet<>(Arrays.asList("Cat", "Dog")));

        mainController = new MainController();

        workspace = mock(Workspace.class);

        mainController.setWorkspace(workspace);

    }

    /**
     * Test RuntimeException on wrong FXML file.
     */
    @Test(expected = RuntimeException.class)
    public void testWrongFXMLFile() {

        mainController.loadFXML("");

    }

    /**
     * Test previous view.
     */
    @Test
    public void testPreviousView() {

        MainController mc = new MainController();
        mc.setWorkspace(workspace);

        BaseController baseController1 = new BaseController(node1);
        BaseController baseController2 = new BaseController(node2);

        mc.setBaseView(baseController1);
        mc.setView(baseController1, baseController2);

        mc.previousView();

        assertEquals(baseController1, mc.viewList.get(0));
        assertEquals(baseController1.getRoot(), mc.view.getChildren().get(0));
        assertFalse(baseController2.getVisibilityProperty().getValue());

    }

    /**
     * Test to view.
     */
    public void testToView() {

        MainController mc = new MainController();
        mc.setWorkspace(workspace);

        BaseController baseController1 = new BaseController(node1);
        BaseController baseController2 = new BaseController(node2);

        mc.setBaseView(baseController1);
        mc.setView(baseController1, baseController2);

        assertTrue(baseController1.getVisibilityProperty().getValue());
        assertTrue(baseController2.getVisibilityProperty().getValue());

        mc.toView(baseController1);

        assertTrue(baseController1.getVisibilityProperty().getValue());
        assertFalse(baseController2.getVisibilityProperty().getValue());

    }

    /**
     * Test to view.
     */
    @Test
    public void testToViewWithNewick() {

        MainController mc = new MainController();
        mc.setWorkspace(workspace);

        BaseController baseController1 = new BaseController(node1);
        BaseController baseController2 = new BaseController(node2);

        mc.setBaseView(baseController1);
        mc.setView(baseController1, baseController2);

        assertTrue(baseController1.getVisibilityProperty().getValue());
        assertTrue(baseController2.getVisibilityProperty().getValue());

        mc.toView(baseController1);

        assertTrue(baseController1.getVisibilityProperty().getValue());
        assertFalse(baseController2.getVisibilityProperty().getValue());

    }

    /**
     * Test the workspace getter.
     */
    @Test
    public void testGetWorkspace() {
        assertEquals(workspace, mainController.getWorkspace());
    }

    /**
     * Test the MenuController getter.
     */
    @Test
    public void testGetMenuController() {
        assertEquals(mainController.menuController, mainController.getMenuController());
    }

    /**
     * Should return the top view that is visible.
     */
    @Test
    public void testTopView() {
        Node node = mock(Node.class);
        when(node.getBaseCounter()).thenReturn(mock(BaseCounter.class));
        ViewController baseView = new BaseController(node);

        mainController.setBaseView(baseView);
        assertEquals(baseView, mainController.topView().get());

        baseView.setVisibility(false);
        assertFalse(mainController.topView().isPresent());
    }

}
