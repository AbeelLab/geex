package nl.tudelft.context.controller;

import de.saxsys.javafx.test.JfxRunner;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 26-4-2015
 */
@RunWith(JfxRunner.class)
public class LoadGraphControllerTest {

    protected final static File nodeFile = new File(LoadGraphControllerTest.class.getResource("/graph/node.graph").getPath());
    protected final static File edgeFile = new File(LoadGraphControllerTest.class.getResource("/graph/edge.graph").getPath());

    protected static final int rulerPoints = 3;
    protected static final int sequencesAmount = 4;

    protected static LoadGraphController loadGraphController;

    protected static final ProgressIndicator progressIndicator = new ProgressIndicator();
    protected static final HBox ruler = new HBox();
    protected static final GridPane sequence = new GridPane();

    /**
     * Setup Load Graph Controller.
     */
    @BeforeClass
    public static void beforeClass() throws Exception {

        loadGraphController = new LoadGraphController(progressIndicator, ruler, sequence);

        loadGraphController.loadGraphService.setNodeFile(nodeFile);
        loadGraphController.loadGraphService.setEdgeFile(edgeFile);

    }

    /**
     * Test RuntimeException on wrong FXML file.
     */
    @Test(expected = RuntimeException.class)
    public void testWrongFXMLFile() {

        loadGraphController.loadFXML("");

    }

    /**
     * Test ruler points and sequences added.
     */
    @Test
    public void testGraph() throws Exception {

        CompletableFuture<Boolean> rulerChildrenAdded = new CompletableFuture<>();

        loadGraphController.ruler.getChildren().addListener((ListChangeListener<? super Node>) event -> {
            if (loadGraphController.ruler.getChildren().size() == rulerPoints) {
                rulerChildrenAdded.complete(true);
            }
        });

        CompletableFuture<Boolean> sequencesAdded = new CompletableFuture<>();

        loadGraphController.sequences.getChildren().addListener((ListChangeListener<? super Node>) event -> {
            if (loadGraphController.sequences.getChildren().size() == sequencesAmount) {
                sequencesAdded.complete(true);
            }
        });

        loadGraphController.loadGraph();

        assertEquals(true, rulerChildrenAdded.get(5000, TimeUnit.MILLISECONDS));
        assertEquals(true, sequencesAdded.get(5000, TimeUnit.MILLISECONDS));

    }

}
