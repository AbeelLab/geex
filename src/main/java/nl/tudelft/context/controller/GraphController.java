package nl.tudelft.context.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import nl.tudelft.context.drawable.DrawableEdge;
import nl.tudelft.context.graph.Graph;
import nl.tudelft.context.graph.Node;
import nl.tudelft.context.service.LoadGraphService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 7-5-2015
 */
public class GraphController extends DefaultController<StackPane> implements Initializable {

    @FXML
    protected ProgressIndicator progressIndicator;
    @FXML
    protected Group sequences;

    protected File nodeFile, edgeFile;
    protected LoadGraphService loadGraphService;

    /**
     * Init a controller at graph.fxml.
     *
     * @param loadGraphService service with file locations
     */
    public GraphController(LoadGraphService loadGraphService) {

        super(new StackPane());

        this.loadGraphService = loadGraphService;

        loadFXML("/application/graph.fxml");

    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        progressIndicator.visibleProperty().bind(loadGraphService.runningProperty());
        loadGraphService.setOnSucceeded(event -> showGraph(loadGraphService.getValue()));

        loadGraph();

    }

    /**
     * Load graph from source.
     */
    protected void loadGraph() {

        sequences.getChildren().clear();
        loadGraphService.restart();

    }

    /**
     * Show graph with reference points.
     */
    protected void showGraph(Graph graph) {

        List<Node> start = new ArrayList<>(Collections.singletonList(graph.getFirstNode()));
        showColumn(graph, start, 0);

        // Bind edges
        graph.edgeSet().stream().forEach(edge -> {
            final DrawableEdge line = new DrawableEdge(graph, edge);
            sequences.getChildren().add(line);
        });

        // Bind nodes
        graph.vertexSet().stream().forEach(node -> {
            final Label label = new Label(Integer.toString(node.getId()));
            label.translateXProperty().bind(node.translateXProperty());
            label.translateYProperty().bind(node.translateYProperty());
            final Tooltip percentages = new Tooltip(node.getBaseCounter().toString());
            label.setTooltip(percentages);
            sequences.getChildren().add(label);

        });

    }

    /**
     * Show the columns of the graph recursive.
     *
     * @param graph  containing the nodes
     * @param nodes  nodes to display
     * @param column column index
     */
    protected void showColumn(Graph graph, List<Node> nodes, int column) {

        if (nodes.isEmpty()) return;

        showNodes(nodes, column);

        List<Node> nextNodes = nodes.stream()
                .map(node -> graph.outgoingEdgesOf(node).stream()
                        .map(graph::getEdgeTarget)
                        .filter(x -> x.incrementIncoming() == graph.inDegreeOf(x)))
                .flatMap(l -> l)
                .collect(Collectors.toList());

        showColumn(graph, nextNodes, column + 1);

    }

    /**
     * Show all nodes at a start position.
     *
     * @param nodes  nodes to draw
     * @param column column to draw at
     */
    protected void showNodes(List<Node> nodes, int column) {

        int shift = nodes.size() * 50;
        int row = 0;
        for (Node node : nodes) {

            node.setTranslateX(column * 100);
            node.setTranslateY(row * 100 - shift);

            row++;

        }

    }

}
