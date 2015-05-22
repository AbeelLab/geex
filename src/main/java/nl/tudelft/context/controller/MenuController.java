package nl.tudelft.context.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import nl.tudelft.context.controller.overlay.ShortcutController;
import nl.tudelft.context.workspace.Workspace;

import java.io.File;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 8-5-2015
 */
public final class MenuController {
    /**
     * Reference to the MainController of the application.
     */
    MainController mainController;

    /**
     * Menubar used in this class.
     */
    MenuBar menuBar;

    /**
     * Menuitems used in other classes.
     */
    MenuItem previous;

    /**
     * Create a new menu.
     *
     * @param mainController The MainController of the application.
     */
    public MenuController(final MainController mainController, final MenuBar menuBar) {
        this.mainController = mainController;
        this.menuBar = menuBar;
        menuBar.setUseSystemMenuBar(true);

        initFileMenu();
        initHelpMenu();

    }

    /**
     * Set up file menu.
     */
    public void initFileMenu() {

        final Menu fileMenu = new Menu("File");

        MenuItem load = new MenuItem("Load Workspace...");
        load.setAccelerator(KeyCombination.keyCombination("ctrl+o"));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Workspace Folder");
        load.setOnAction(event -> {
            Window window = mainController.getRoot().getScene().getWindow();
            File workspaceDirectory = directoryChooser.showDialog(window);
            if (workspaceDirectory != null) {
                Workspace workspace = new Workspace(workspaceDirectory);
                if (workspace.load()) {
                    mainController.displayMessage(MessageController.SUCCESS_LOAD_WORKSPACE);
                } else {
                    mainController.displayMessage(MessageController.FAIL_LOAD_WORKSPACE);
                }
                mainController.setWorkspace(workspace);
                mainController.setBaseView(new NewickController(mainController));
            }
        });

        previous = new MenuItem("Previous");
        previous.setAccelerator(new KeyCodeCombination(KeyCode.ESCAPE));
        previous.setOnAction(event -> mainController.previousView());

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> mainController.exitProgram());

        fileMenu.getItems().addAll(load, previous, exit);
        menuBar.getMenus().add(fileMenu);

    }

    /**
     * Set up help menu.
     */
    public void initHelpMenu() {

        final Menu menuHelp = new Menu("Help");

        MenuItem shortcuts = new MenuItem("Shortcuts");
        shortcuts.setAccelerator(KeyCombination.keyCombination("f1"));
        shortcuts.setOnAction(event -> mainController.setOverlay(new ShortcutController()));

        menuHelp.getItems().addAll(shortcuts);

        menuBar.getMenus().add(menuHelp);

    }

    /**
     * Set the action to execute when the previous button is pressed.
     *
     * @param e Action to execute
     */
    public void setPreviousAction(final EventHandler<ActionEvent> e) {
        previous.setOnAction(e);
    }
}
