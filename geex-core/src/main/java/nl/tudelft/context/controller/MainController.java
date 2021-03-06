package nl.tudelft.context.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import nl.tudelft.context.breadcrumb.Breadcrumb;
import nl.tudelft.context.controller.overlay.OverlayController;
import nl.tudelft.context.window.WindowFactory;
import nl.tudelft.context.workspace.Workspace;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author René Vennik
 * @version 1.0
 * @since 25-4-2015
 */
public class MainController extends AbstractController<StackPane> {

    /**
     * FXML stack panes in view.
     */
    @FXML
    StackPane view, overlay;

    /**
     * Menu bar from FXML.
     */
    @FXML
    MenuBar menu;

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
     * The MenuController that needs to be changed whenever the current view changes.
     */
    MenuController menuController;

    /**
     * The last top view that was seen.
     */
    Optional<ViewController> previousTopView = Optional.empty();

    /**
     * Init a controller at main.fxml.
     */
    public MainController() {
        super(new StackPane());

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
        menuController = new MenuController(this, menu);

        messageController = new MessageController();
        main.setBottom(messageController.getRoot());

        new OverlayController(this, overlay);

        setBaseView(new WelcomeController(this,
                menuController.getSelectWorkspace(),
                menuController.getSelectRecentWorkspace()));
    }

    /**
     * Set a new base view (clear the stack).
     *
     * @param viewController Controller containing JavaFX root
     */
    public final void setBaseView(final ViewController viewController) {

        view.getChildren().setAll(viewController.getRoot());
        viewList.setAll(viewController);

        activateView();

    }

    /**
     * Hide overlay.
     *
     * @return If overlay was shown.
     */
    public boolean hideOverlay() {

        boolean visible = overlay.isVisible();
        if (visible) {
            menuController.getToggleOverlay().fire();
        }

        return visible;

    }

    /**
     * Set a new main view and push it on the view stack.
     *
     * @param on             Controller to stack this view on
     * @param viewController Controller containing JavaFX root
     * @param newWindow      True to force a new window
     */
    public void setView(final ViewController on, final ViewController viewController, final boolean newWindow) {
        hideOverlay();

        if (newWindow || viewList.indexOf(on) == -1) {
            openWindow(viewController);
        } else {
            setLocalView(on, viewController);
        }
    }

    /**
     * Set a new main view and push it on the view stack.
     *
     * @param on             Controller to stack this view on
     * @param viewController Controller containing JavaFX root
     */
    public void setView(final ViewController on, final ViewController viewController) {
        setView(on, viewController, false);
    }

    /**
     * Create a new window for this viewController.
     *
     * @param viewController ViewController to show in this Window.
     */
    private void openWindow(final ViewController viewController) {
        WindowFactory.create(viewController.getBreadcrumbName(), viewController.getRoot());
    }

    /**
     * Navigate to a new view.
     *
     * @param on             Previous view
     * @param viewController Next view
     */
    private void setLocalView(final ViewController on, final ViewController viewController) {
        viewList.remove(viewList.indexOf(on) + 1, viewList.size());
        view.getChildren().retainAll(viewList.stream().map(ViewController::getRoot).collect(Collectors.toList()));
        viewList.add(viewController);
        viewController.setVisibility(true);
        view.getChildren().add(viewController.getRoot());

        activateView();
    }

    /**
     * Creates a stream of visible view controllers.
     *
     * @return A stream of visible view controllers
     */
    private Stream<ViewController> getVisibleStream() {
        return viewList.stream().filter(viewController -> viewController.getVisibilityProperty().getValue());
    }

    /**
     * Set the previous view as view.
     */
    public final void previousView() {

        if (!hideOverlay()) {

            getVisibleStream()
                    .skip(1)
                    .reduce((previous, current) -> current)
                    .ifPresent(viewController -> viewController.setVisibility(false));

            activateView();

        }

    }

    /**
     * Go the a certain view.
     *
     * @param viewController View to go to
     */
    public void toView(final ViewController viewController) {

        hideOverlay();

        int index = viewList.indexOf(viewController) + 1;
        viewList.stream()
                .skip(index)
                .forEach(vc -> vc.setVisibility(false));
        viewList.stream()
                .limit(index)
                .forEach(vc -> vc.setVisibility(true));

        activateView();

    }

    /**
     * Gets the controller at the top, which should be visible to the user.
     *
     * @return the an option to the top ViewController that is visible
     */
    public final Optional<ViewController> topView() {

        return getVisibleStream().reduce((previous, current) -> current);

    }

    /**
     * Activates the top visible ViewController.
     */
    public final void activateView() {
        Optional<ViewController> nextTopView = topView();
        previousTopView.ifPresent(previousView -> previousView.setActivated(false));
        nextTopView.ifPresent(nextView -> nextView.setActivated(true));
        previousTopView = nextTopView;
    }

    /**
     * Exits the program.
     */
    public final void exitProgram() {
        Platform.exit();
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
     * Set the current workspace. And also preload it.
     * Gets the MenuController.
     *
     * @return The MenuController
     */
    public MenuController getMenuController() {
        return menuController;
    }

    /**
     * Set the current workspace. And also preload it.
     *
     * @param workspace The new workspace
     */
    public final void setWorkspace(final Workspace workspace) {
        if (this.workspace != null) {
            this.workspace.close();
        }
        this.workspace = workspace;
        workspace.preload();
    }

    /**
     * Show the graph.
     *
     * @param on      Controller to place it on
     * @param sources Sources to display
     */
    public void showGraph(final NewickController on, final Set<String> sources) {
        this.setView(on, new GraphController(this, sources,
                workspace.getGraph(),
                workspace.getCodingSequence(),
                workspace.getResistance()));
    }
}
