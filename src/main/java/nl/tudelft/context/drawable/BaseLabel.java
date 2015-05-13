package nl.tudelft.context.drawable;

import javafx.scene.shape.Rectangle;
import nl.tudelft.context.controller.GraphController;

/**
 * @author Jasper on 13-5-2015.
 * @version 1.0
 * @since 13-5-2015
 */
public class BaseLabel extends Rectangle {

    public static final int BASE_HEIGHT = 5;

    public BaseLabel(char base, float ratio) {

        super(Math.round(ratio * GraphController.NODE_WIDTH), BASE_HEIGHT);
        getStyleClass().add("node-label-" + base);

    }

}