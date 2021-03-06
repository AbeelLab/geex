package nl.tudelft.context.controller.search;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gerben Oolbekkink
 * @version 1.0
 * @since 12-6-2015
 */
public class NewickSearchController extends HBox {
    /**
     * Classname for buttons.
     */
    private static String buttonClass = "my-button";
    /**
     * Class name for focused search.
     */
    private static String focusSearchClass = "search-focus";
    /**
     * Class name for found search.
     */
    private static String searchClass = "search";

    /**
     * List of labels in the current Newick.
     */
    private List<Label> labels;

    /**
     * ScrollPane to move when focusing a label.
     */
    private ScrollPane scrollPane;

    /**
     * TextField used for a search query.
     */
    TextField searchField;

    /**
     * Buttons used for moving the cursor to the previous/next label.
     */
    private Button searchPrev, searchNext;

    /**
     * List of currently found labels.
     */
    private List<Label> selectedLabels = new ArrayList<>();
    /**
     * Index of the active found node.
     */
    private int searchIndex;

    /**
     * Create a new NewickSearchController.
     *
     * @param labels     Nodes to search in.
     * @param scrollPane Scroller containing the Labels in labels.
     */
    public NewickSearchController(final List<Label> labels, final ScrollPane scrollPane) {
        this.labels = labels;
        this.scrollPane = scrollPane;

        searchField = new TextField();
        searchPrev = new Button("\u25b2");
        searchNext = new Button("\u25bc");

        searchField.setPromptText("Search strains...");
        searchField.getStyleClass().add("searchbox");
        searchPrev.getStyleClass().add(buttonClass);
        searchNext.getStyleClass().add(buttonClass);

        getChildren().setAll(searchField, searchNext, searchPrev);

        searchField.setOnAction(event1 -> searchNext.fire());
        searchField.setOnKeyReleased(searchFieldEventHandler());
        searchNext.setOnAction(searchMoveEventHandler(1));
        searchPrev.setOnAction(searchMoveEventHandler(-1));
    }

    /**
     * Create an eventhandler for searching.
     *
     * @return Code to be executed when searching.
     */
    public EventHandler<KeyEvent> searchFieldEventHandler() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER) {
                return;
            }
            selectedLabels = NewickSearchController.this.search(searchField.getText());
            searchIndex = 0;

            if (!selectedLabels.isEmpty()) {
                NewickSearchController.this.ensureVisible(selectedLabels.get(searchIndex));
            }
        };
    }

    /**
     * Create an eventhandler for moving the search.
     *
     * @param dir Direction to move into. (1 or -1)
     * @return Code to be executed when moving.
     */
    public EventHandler<ActionEvent> searchMoveEventHandler(final int dir) {
        return event -> {
            if (!selectedLabels.isEmpty()) {
                selectedLabels.forEach(label -> label.getStyleClass().remove(focusSearchClass));

                searchIndex += dir + selectedLabels.size();
                searchIndex %= selectedLabels.size();

                ensureVisible(selectedLabels.get(searchIndex));
            }
        };
    }

    /**
     * Perform a search operation.
     *
     * @param query Query to search for.
     * @return A list of found Labels.
     */
    public List<Label> search(final String query) {
        labels.stream().forEach(label -> label.getStyleClass().removeAll(searchClass, focusSearchClass));
        if (query.length() < 1) {
            return new ArrayList<>();
        }

        List<Label> selected = labels.stream()
                .filter(label -> label.getText().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        selected.forEach(label -> label.getStyleClass().add(searchClass));

        return selected;
    }

    /**
     * Make sure this Node is visible in the current scrollPane.
     *
     * @param node Node to be made visible.
     */
    private void ensureVisible(final Node node) {
        double width = scrollPane.getContent().getBoundsInLocal().getWidth();
        double height = scrollPane.getContent().getBoundsInLocal().getHeight();

        double x = node.getBoundsInParent().getMaxX();
        double y = node.getBoundsInParent().getMaxY();

        // scrolling values range from 0 to 1
        scrollPane.setVvalue(y / height);
        scrollPane.setHvalue(x / width);

        node.getStyleClass().add(focusSearchClass);
    }
}
