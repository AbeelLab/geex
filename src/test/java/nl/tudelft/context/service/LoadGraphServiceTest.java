package nl.tudelft.context.service;

import de.saxsys.javafx.test.JfxRunner;
import javafx.concurrent.Worker;
import nl.tudelft.context.graph.Graph;
import nl.tudelft.context.graph.GraphFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 26-4-2015
 */
@RunWith(JfxRunner.class)
public class LoadGraphServiceTest {

    protected final static File nodeFile = new File(LoadGraphServiceTest.class.getResource("/graph/node.graph").getPath());
    protected final static File edgeFile = new File(LoadGraphServiceTest.class.getResource("/graph/edge.graph").getPath());

    protected static Graph graphFromFactory;

    /**
     * Set up comparing graphFromFactory.
     */
    @BeforeClass
    public static void beforeClass() throws FileNotFoundException {

        GraphFactory graphFactory = new GraphFactory();
        graphFromFactory = graphFactory.getGraph(nodeFile, edgeFile);

    }

    /**
     * Test if the graphFromFactory load succeeds.
     */
    @Test
    public void testGraphLoadSucceeds() throws Exception {

        final LoadGraphService loadGraphService = new LoadGraphService();
        loadGraphService.setNodeFile(nodeFile);
        loadGraphService.setEdgeFile(edgeFile);

        CompletableFuture<Graph> completableFuture = new CompletableFuture<>();

        loadGraphService.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                completableFuture.complete(loadGraphService.getValue());
            }
        });

        loadGraphService.restart();

        // Wait for graphFromFactory service
        Graph graph = completableFuture.get(5000, TimeUnit.MILLISECONDS);

        assertNotNull(graph);
        assertEquals(graphFromFactory.getReferencePoints(), graph.getReferencePoints());
        assertEquals(graphFromFactory.getVertexById(0), graph.getVertexById(0));
        assertEquals(graphFromFactory.getVertexById(1), graph.getVertexById(1));
        assertEquals(graphFromFactory.getVertexById(2), graph.getVertexById(2));
        assertEquals(graphFromFactory.getVertexById(3), graph.getVertexById(3));

    }

}
