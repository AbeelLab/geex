package nl.tudelft.context.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import nl.tudelft.context.breadcrumb.Breadcrumb;
import nl.tudelft.context.workspace.Workspace;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 25-4-2015
 */
public class MainController extends DefaultController<BorderPane> {

    /**
     * The container of all views after this one.
     */
    @FXML
    StackPane view;

    /**
     * FXML pointer for right BorderPane.
     */
    @FXML
    BorderPane main;

    /**
     * A list of the current views.
     */
    ObservableList<ViewController> viewList = FXCollections.observableList(new ArrayList<>());

    /**
     * The current workspace.
     */
    private Workspace workspace;

    /**
     * The MessageController that is needed to display error messages.
     */
    MessageController messageController;

    /**
     * If Newick is lifted.
     */
    BooleanProperty newickLifted = new SimpleBooleanProperty(false);

    /**
     * Init a controller at main.fxml.
     */
    public MainController() {

        super(new BorderPane());

        loadFXML("/application/main.fxml");

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
    public final void initialize(final URL location, final ResourceBundle resources) {

        main.setTop(new Breadcrumb(this, viewList));
        root.setTop(new MenuController(this));

        messageController = new MessageController();
        main.setBottom(messageController.getRoot());

        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                previousView();
            }
            if (event.getCode() == KeyCode.L) {
                toggleNewick();
            }
        });

    }

    /**
     * Set a new base view (clear the stack).
     *
     * @param viewController Controller containing JavaFX root
     */
    public final void setBaseView(final ViewController viewController) {

        view.getChildren().setAll(viewController.getRoot());
        viewList.setAll(viewController);

    }

    /**
     * Set a new main view and push it on the view stack.
     *
     * @param on             Controller to stack this view on
     * @param viewController Controller containing JavaFX root
     */
    public final void setView(final ViewController on, final ViewController viewController) {

        if (newickLifted.getValue()) {
            toggleNewick();
        }

        viewList.remove(viewList.indexOf(on) + 1, viewList.size());
        viewList.add(viewController);
        view.getChildren().retainAll(viewList.stream().map(ViewController::getRoot).collect(Collectors.toList()));
        view.getChildren().add(viewController.getRoot());

    }

    /**
     * Set the previous view as view.
     */
    public final void previousView() {

        if (newickLifted.getValue()) {
            toggleNewick();
        } else {
            List<ViewController> visibleViews = viewList.filtered(viewController ->
                    viewController.getVisibilityProperty().getValue());
            if (visibleViews.size() > 1) {
                visibleViews.get(visibleViews.size() - 1).setVisibility(false);
            }
        }

    }

    /**
     * Go the a certain view.
     *
     * @param viewController View to go to
     */
    public void toView(final ViewController viewController) {

        if (newickLifted.getValue()) {
            toggleNewick();
        }

        int index = viewList.indexOf(viewController);
        viewList.stream()
                .skip(index + 1)
                .forEach(vc -> vc.setVisibility(false));
        viewList.stream()
                .limit(index + 1)
                .forEach(vc -> vc.setVisibility(true));

    }

    /**
     * Toggle the newick view on top of everything else.
     */
    public void toggleNewick() {

        newickLifted.setValue(!newickLifted.getValue());

    }

    /**
     * Exits the program.
     */
    public final void exitProgram() {
        System.exit(0);
    }

    /**
     * Get the current workspace.
     *
     * @return The current workspace
     */
    public Workspace getWorkspace() {
        return workspace;
    }

    /**
     * Set the current workspace.
     *
     * @param workspace The new workspace
     */
    public final void setWorkspace(final Workspace workspace) {
        this.workspace = workspace;
    }


    /**
     * The function that is used to display a message in the footer.
     *
     * @param text The text that will be displayed.
     */
    public final void displayMessage(final String text) {
        messageController.displayMessage(text);
    }
}
