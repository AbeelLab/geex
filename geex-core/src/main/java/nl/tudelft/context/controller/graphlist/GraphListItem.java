package nl.tudelft.context.controller.graphlist;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import nl.tudelft.context.model.graph.StackGraph;

/**
 * @author René Vennik
 * @version 1.0
 * @since 17-6-2015
 */
public class GraphListItem extends Pane {

    public GraphListItem(StackGraph graph, ObjectProperty<StackGraph> activeGraphProperty) {

        getStyleClass().add("graph-item");
        if (graph.equals(activeGraphProperty.get())) {
            getStyleClass().add("active");
        }
        setOnMouseClicked(event -> activeGraphProperty.set(graph));
        getChildren().add(new Label(graph.getName()));

    }

}
