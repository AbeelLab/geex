package nl.tudelft.context.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * @author René Vennik
 * @version 1.0
 * @since 8-5-2015
 */
public final class MenuController {

    /**
     * Reference to the MainController of the application.
     */
    MainController mainController;

    /**
     * The menu items.
     */
    private MenuItem
            loadGenomeGraph,
            toggleOverlay,
            toggleSelect,
            selectWorkspace,
            resetView;

    /**
     * The menu's.
     */
    private Menu
            selectRecentWorkspace;

    /**
     * FXML menu bar.
     */
    MenuBar menuBar;

    /**
     * Create a new menu.
     *
     * @param mainController The MainController of the application.
     * @param menuBar        The menubar this Menu should hook onto.
     */
    public MenuController(final MainController mainController, final MenuBar menuBar) {

        this.mainController = mainController;
        this.menuBar = menuBar;

        initFileMenu();
        initNavigateMenu();
        initHelpMenu();

    }

    /**
     * Initialize file menu.
     */
    private void initFileMenu() {

        selectWorkspace = createDisabledMenuItem("Select workspace folder",
                new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));

        selectRecentWorkspace = new Menu("_Select recent workspace");
        selectRecentWorkspace.setDisable(true);

        menuBar.getMenus().add(createMenu("_File",
                selectWorkspace,
                selectRecentWorkspace,
                createMenuItem("Exit", null,
                        event -> mainController.exitProgram())));
    }

    /**
     * Initialize file menu.
     */
    private void initNavigateMenu() {

        loadGenomeGraph = createDisabledMenuItem("Load Genome graph",
                new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));

        toggleSelect = createDisabledMenuItem("Show Phylogenetic tree",
                new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN));

        resetView = createDisabledMenuItem("Reset the view",
                new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN));

        menuBar.getMenus().add(createMenu("_Navigate",
                createMenuItem("Previous view",
                        new KeyCodeCombination(KeyCode.ESCAPE),
                        event -> mainController.previousView()),
                toggleSelect,
                loadGenomeGraph,
                resetView));

    }

    /**
     * Initialize help menu.
     */
    private void initHelpMenu() {

        toggleOverlay = createMenuItem("Shortcuts",
                new KeyCodeCombination(KeyCode.F1),
                null);

        menuBar.getMenus().add(createMenu("_Help",
                toggleOverlay));

    }

    /**
     * The function that will create a menu with an undefined number of menuItems.
     *
     * @param title     Title of the menu.
     * @param menuItems The menu items that will be added to this menu.
     * @return Menu with the menuItems attached.
     */
    private Menu createMenu(final String title, final MenuItem... menuItems) {

        final Menu menu = new Menu(title);
        menu.getItems().addAll(menuItems);
        return menu;

    }

    /**
     * Create a disabled menu item, all of these don't have an action yet and are disabled.
     *
     * @param title   The title of the menuItem
     * @param keyComb The keycombination for this menuItem
     * @return A new menuItem
     */
    private MenuItem createDisabledMenuItem(final String title, final KeyCombination keyComb) {
        MenuItem menuItem = createMenuItem(title, keyComb, null);
        menuItem.setDisable(true);
        return menuItem;
    }

    /**
     * The function that returns a menuItem with a title, shift and event attached.
     *
     * @param title   The title of the menuItem.
     * @param keyComb The shift to this menuItem.
     * @param event   Event that the item will use.
     * @return The menuItem with the parameters attached.
     */
    private MenuItem createMenuItem(final String title,
                                    final KeyCombination keyComb,
                                    final EventHandler<ActionEvent> event) {

        final MenuItem menuItem = new MenuItem(title);
        menuItem.setAccelerator(keyComb);
        menuItem.setOnAction(event);

        return menuItem;

    }

    /**
     * Get the menu item to select a workspace.
     *
     * @return MenuItem for the welcomeController.
     */
    public MenuItem getSelectWorkspace() {
        return selectWorkspace;
    }

    /**
     * Get the menu item to select a recent workspace.
     *
     * @return Menu for the welcomeController.
     */
    public Menu getSelectRecentWorkspace() {
        return selectRecentWorkspace;
    }

    /**
     * Get the menu item to load the genome graph.
     *
     * @return The menu item to load the genome graph
     */
    public MenuItem getLoadGenomeGraph() {
        return loadGenomeGraph;
    }

    /**
     * Get the menu item to toggle the overlay.
     *
     * @return The menu item to toggle the overlay
     */
    public MenuItem getToggleOverlay() {
        return toggleOverlay;
    }

    /**
     * Get the menu item to toggle the select view.
     *
     * @return The menu item to toggle the select view
     */
    public MenuItem getToggleSelect() {
        return toggleSelect;
    }

    /**
     * Get the menu item that resets the view.
     *
     * @return The menu item that sets the view.
     */
    public MenuItem getResetView() {
        return resetView;
    }

}
