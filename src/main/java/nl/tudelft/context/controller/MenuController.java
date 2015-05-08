package nl.tudelft.context.controller;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 8-5-2015
 */
public class MenuController extends MenuBar {

    protected MainController mainController;

    /**
     * Create a new menu.
     */
    public MenuController(MainController mainController) {

        this.mainController = mainController;

        initFileMenu();
        initHelpMenu();

    }

    /**
     * Set up file menu.
     */
    public void initFileMenu() {

        final Menu fileMenu = new Menu("File");

        MenuItem load = new MenuItem("Load...");

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