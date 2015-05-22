package nl.tudelft.context.controller;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import nl.tudelft.context.workspace.Workspace;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 8-5-2015
 */
public final class MenuController extends MenuBar {
    /**
     * Reference to the MainController of the application.
     */
    MainController mainController;

    /**
     * Create a new menu.
     *
     * @param mainController The MainController of the application.
     */
    public MenuController(final MainController mainController) {

        this.mainController = mainController;
        this.setUseSystemMenuBar(true);

        initFileMenu();
        initHelpMenu();

    }

    /**
     * Set up file menu.
     */
    public void initFileMenu() {

        final Menu fileMenu = new Menu("File");

        MenuItem load = new MenuItem("Load Workspace...");
        load.setOnAction(event -> {
            Workspace.chooseWorkspace(mainController);

        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> mainController.exitProgram());

        fileMenu.getItems().addAll(load, exit);
        getMenus().add(fileMenu);

    }

    /**
     * Set up help menu.
     */
    public void initHelpMenu() {

        final Menu menuHelp = new Menu("Help");
        getMenus().add(menuHelp);

    }

}
