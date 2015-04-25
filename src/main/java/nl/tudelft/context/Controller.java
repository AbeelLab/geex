package nl.tudelft.context;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import nl.tudelft.context.graph.Graph;
import nl.tudelft.context.graph.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 25-4-2015
 */
public class Controller extends ScrollPane implements Initializable {

    @FXML
    protected GridPane ruler;

    protected Graph graph;

    /**
     * Init a controller at main.fxml.
     *
     * @param graph graph to display in view
     * @throws RuntimeException
     */
    public Controller(Graph graph) {

        this.graph = graph;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/main.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        initRuler();

        reverseScroll();

    }

    /**
     * Init ruler with reference points.
     */
    protected void initRuler() {

        int row = 0;
        for (int referencePoint : graph.getReferencePoints()) {

            final Label label = new Label(Integer.toString(referencePoint));
            ruler.add(label, row, 0);
            showNodes(graph.getVertexesByStartPosition(referencePoint), row);
            row++;

            if (row >= 100) break; // Only show first 100 for now

        }

    }

    protected void showNodes(Set<Node> nodes, int row) {

        if (nodes == null) return;

        int col = 1;
        for (Node node : nodes) {
            ruler.add(new SequenceController(node), row, col);
            col++;
        }

    }

    /**
     * On vertical scroll, make it horizontal.
     */
    protected void reverseScroll() {

        this.setOnScroll(event -> {
            final double displacement = event.getDeltaY() / this.getContent().getBoundsInLocal().getWidth();
            this.setHvalue(this.getHvalue() - displacement);
        });

    }

}
