package nl.tudelft.context.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import nl.tudelft.context.controller.graphlist.GraphFilterController;
import nl.tudelft.context.controller.locator.LocatorController;
import nl.tudelft.context.drawable.graph.DrawableGraph;
import nl.tudelft.context.logger.Log;
import nl.tudelft.context.logger.message.Message;
import nl.tudelft.context.model.annotation.CodingSequenceMap;
import nl.tudelft.context.model.annotation.ResistanceMap;
import nl.tudelft.context.model.graph.GraphMap;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author René Vennik
 * @version 1.0
 * @since 7-5-2015
 */
public class GraphController extends AbstractGraphController {

    /**
     * The graph list.
     */
    @FXML
    VBox graphs;

    /**
     * The locator.
     */
    @FXML
    Pane locator;

    /**
     * Sources that are displayed in the graph.
     */
    Set<String> sources;

    /**
     * Select controller to select strains.
     */
    SelectNewickController selectNewickController = new SelectNewickController(
            mainController,
            this,
            mainController.getWorkspace().getNewick()
    );

    /**
     * Property with graph map.
     */
    ReadOnlyObjectProperty<GraphMap> graphMapIn;

    /**
     * Sources that are displayed in the graph.
     * Property with codingSequence map.
     */
    ReadOnlyObjectProperty<CodingSequenceMap> codingSequenceMapIn;

    /**
     * Property with resistance map.
     */
    ReadOnlyObjectProperty<ResistanceMap> resistanceMapIn;

    /**
     * Controller for the current graph.
     */
    GraphFilterController graphFilterController;

    /**
     * Init a controller at graph.fxml.
     *
     * @param mainController      MainController for the application
     * @param sources             Sources to display
     * @param graphMapIn          The graphMap from the workspace, might not be loaded.
     * @param codingSequenceMapIn The CodingSequenceMap from the workspace, might not be loaded.
     * @param resistanceMapIn     The ResistanceMap from the workspace, might not be loaded.
     */
    public GraphController(final MainController mainController,
                           final Set<String> sources,
                           final ReadOnlyObjectProperty<GraphMap> graphMapIn,
                           final ReadOnlyObjectProperty<CodingSequenceMap> codingSequenceMapIn,
                           final ReadOnlyObjectProperty<ResistanceMap> resistanceMapIn) {

        super(mainController);
        this.sources = sources;

        this.graphMapIn = graphMapIn;
        this.codingSequenceMapIn = codingSequenceMapIn;
        this.resistanceMapIn = resistanceMapIn;

        loadFXML("/application/graph.fxml");
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        super.initialize(location, resources);

        graphFilterController = new GraphFilterController(graphs);
        LocatorController locatorController = new LocatorController(locator, nodeMapProperty, this);
        positionProperty.addListener((observable, oldValue, newValue) -> locatorController.updatePosition(newValue));

        initProperties();
        initMenu();

    }

    /**
     * Bind the properties from the workspace to handles.
     */
    private void initProperties() {

        ObjectProperty<GraphMap> graphMapProperty = new SimpleObjectProperty<>();
        ObjectProperty<CodingSequenceMap> codingSequenceMapProperty = new SimpleObjectProperty<>();
        ObjectProperty<ResistanceMap> resistanceMapProperty = new SimpleObjectProperty<>();

        graphMapProperty.addListener(event -> {
            Log.info(Message.SUCCESS_LOAD_CODING_SEQUENCE);
            loadGraph(graphMapProperty.get(), codingSequenceMapProperty.get(), resistanceMapProperty.get());
        });
        codingSequenceMapProperty.addListener(event ->
                loadGraph(graphMapProperty.get(), codingSequenceMapProperty.get(), resistanceMapProperty.get()));
        resistanceMapProperty.addListener(event ->
                loadGraph(graphMapProperty.get(), codingSequenceMapProperty.get(), resistanceMapProperty.get()));

        graphMapProperty.bind(graphMapIn);
        codingSequenceMapProperty.bind(codingSequenceMapIn);
        resistanceMapProperty.bind(resistanceMapIn);

        progressIndicator.visibleProperty().bind(graphMapProperty.isNull());

    }

    /**
     * Bind the menu buttons.
     */
    private void initMenu() {

        MenuController menuController = mainController.getMenuController();

        MenuItem toggleSelect = menuController.getToggleSelect();
        activeProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                toggleSelect.setOnAction(event -> mainController.setView(this, selectNewickController));
                toggleSelect.disableProperty().bind(activeProperty.not());
            }
        });

        MenuItem resetView = menuController.getResetView();
        resetView.setOnAction(event -> resetView());
        resetView.disableProperty().bind(activeProperty.not());

    }

    /**
     * Load graph from source.
     *
     * @param graphMap          The GraphMap which is loaded.
     * @param codingSequenceMap The CodingSequenceMap which is loaded.
     * @param resistanceMap     The ResistanceMap which is loaded.
     */
    private void loadGraph(final GraphMap graphMap, final CodingSequenceMap codingSequenceMap,
                           final ResistanceMap resistanceMap) {
        if (graphMap != null && codingSequenceMap != null && resistanceMap != null) {
            graphMap.setCodingSequence(codingSequenceMap);
            graphMap.setResistance(resistanceMap);
            graphFilterController.setBaseGraph(graphMap.flat(sources));
            graphFilterController.reset();

            graphFilterController.getActiveGraphProperty().addListener((observable, oldValue, newValue) ->
                    showGraph(new DrawableGraph(newValue)));
            showGraph(new DrawableGraph(graphFilterController.getActiveGraph()));
        }
    }

    /**
     * Update the selected sources.
     *
     * @param sources New selected sources.
     */
    public void updateSelectedSources(final Set<String> sources) {
        selectedSources.setValue(sources);
    }

    @Override
    public String getBreadcrumbName() {
        return "Genome graph (" + sources.size() + ")";
    }

    /**
     * Function that resets the view to the most zoomed out level.
     */
    private void resetView() {
        graphFilterController.reset();
    }
}
