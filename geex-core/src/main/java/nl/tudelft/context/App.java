package nl.tudelft.context;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.tudelft.context.controller.MainController;
import nl.tudelft.context.logger.Log;
import nl.tudelft.context.logger.StdOutLogger;
import nl.tudelft.context.logger.message.Message;
import nl.tudelft.context.window.Window;

/**
 * Entry point of the App.
 *
 * @author René Vennik
 * @version 1.0
 * @since 23-4-2015
 */
public class App extends Application {

    /**
     * @param args arguments
     */
    public static void main(final String... args) {
        if (Boolean.getBoolean("debug")) {
            Log.instance().addLogger(new StdOutLogger());
        }

        launch(args);

    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage the primary stage for this application.
     */
    @Override
    public final void start(final Stage stage) {
        Log.debug(Message.APPLICATION_STARTING);

        MainController controller = new MainController();

        Window window = new Window("Geex", controller.getRoot());
        window.show();

    }

}
