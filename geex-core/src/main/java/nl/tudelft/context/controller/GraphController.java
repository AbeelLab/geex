package nl.tudelft.context.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.MenuItem;
import nl.tudelft.context.drawable.graph.DrawableGraph;
import nl.tudelft.context.logger.Log;
import nl.tudelft.context.logger.message.Message;
import nl.tudelft.context.model.annotation.AnnotationMap;
import nl.tudelft.context.model.annotation.CodingSequence;
import nl.tudelft.context.model.annotation.Resistance;
import nl.tudelft.context.model.graph.CollapseGraph;
import nl.tudelft.context.model.graph.GraphMap;
import nl.tudelft.context.model.graph.InsertDeleteGraph;
import nl.tudelft.context.model.graph.SinglePointGraph;
import nl.tudelft.context.model.graph.UnknownGraph;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author René Vennik
 * @version 1.0
 * @since 7-5-2015
 */
public final class GraphController extends AbstractGraphController {

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
     * Property with annotation map.
     */
    ReadOnlyObjectProperty<AnnotationMap<CodingSequence>> annotationMapIn;

    /**
     * Property with resistance map.
     */
    ReadOnlyObjectProperty<AnnotationMap<Resistance>> resistanceMapIn;

    /**
     * Init a controller at graph.fxml.
     *
     * @param mainController  MainController for the application
     * @param sources         Sources to display
     * @param graphMapIn      The graphMap from the workspace, might not be loaded.
     * @param annotationMapIn The CodingSequenceMap from the workspace, might not be loaded.
     * @param resistanceMapIn The ResistanceMap from the workspace, might not be loaded.
     */
    public GraphController(final MainController mainController,
                           final Set<String> sources,
                           final ReadOnlyObjectProperty<GraphMap> graphMapIn,
                           final ReadOnlyObjectProperty<AnnotationMap<CodingSequence>> annotationMapIn,
                           final ReadOnlyObjectProperty<AnnotationMap<Resistance>> resistanceMapIn) {

        super(mainController);
        this.sources = sources;

        this.graphMapIn = graphMapIn;
        this.annotationMapIn = annotationMapIn;
        this.resistanceMapIn = resistanceMapIn;

        loadFXML("/application/graph.fxml");
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        super.initialize(location, resources);

        ObjectProperty<GraphMap> graphMapProperty = new SimpleObjectProperty<>();
        ObjectProperty<AnnotationMap> annotationMapProperty = new SimpleObjectProperty<>();
        ObjectProperty<AnnotationMap> resistanceMapProperty = new SimpleObjectProperty<>();

        graphMapProperty.addListener(event -> {
            Log.info(Message.SUCCESS_LOAD_ANNOTATION);
            loadGraph(graphMapProperty.get(), annotationMapProperty.get(), resistanceMapProperty.get());
        });
        annotationMapProperty.addListener(event -> loadGraph(graphMapProperty.get(), annotationMapProperty.get(), resistanceMapProperty.get()));
        resistanceMapProperty.addListener(event -> loadGraph(graphMapProperty.get(), annotationMapProperty.get(), resistanceMapProperty.get()));

        resistanceMapProperty.addListener((observable, oldValue, newValue) -> {
            loadResistance(newValue);
        });

        graphMapProperty.bind(graphMapIn);
        annotationMapProperty.bind(annotationMapIn);
        resistanceMapProperty.bind(resistanceMapIn);

        progressIndicator.visibleProperty().bind(graphMapProperty.isNull());

        initMenu();

    }

    /**
     * Bind the menu buttons.
     */
    private void initMenu() {

        MenuController menuController = mainController.getMenuController();

        MenuItem zoomIn = menuController.getZoomIn();
        zoomIn.setOnAction(event -> zoomIn());
        zoomIn.disableProperty().bind(activeProperty.not());

        MenuItem zoomOut = menuController.getZoomOut();
        zoomOut.setOnAction(event -> zoomOut());
        zoomOut.disableProperty().bind(activeProperty.not());

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
     * @param graphMap      The GraphMap which is loaded.
     * @param codingSequenceMap The CodingSequenceMap which is loaded.
     * @param resistanceMap The ResistanceMap which is loaded.
     */
    private void loadGraph(final GraphMap graphMap, final AnnotationMap codingSequenceMap, final AnnotationMap resistanceMap) {
        if (graphMap != null && codingSequenceMap != null && resistanceMap != null) {
            graphMap.setAnnotations(codingSequenceMap);
            graphMap.setResistance(resistanceMap);
            graphList.add(graphMap.flat(sources));
            graphList.add(new SinglePointGraph(graphList.getLast()));
            graphList.add(new InsertDeleteGraph(graphList.getLast()));
            graphList.add(new CollapseGraph(graphList.getLast()));
            graphList.add(new UnknownGraph(graphList.getLast()));

            currentGraph = graphList.getLast();
            DrawableGraph drawableGraph = new DrawableGraph(currentGraph);
            showGraph(drawableGraph);
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

    /**
     * Load resistances from source.
     *
     * @param resistanceMap The resistance map which is loaded.
     */
    private void loadResistance(final AnnotationMap resistanceMap) {
        Log.info(Message.SUCCESS_LOAD_RESISTANCE);
    }

    @Override
    public String getBreadcrumbName() {
        return "Genome graph (" + sources.size() + ")";
    }

    /**
     * Function that resets the view to the most zoomed out level.
     */
    private void resetView() {
        if (graphList.indexOf(currentGraph) != graphList.size() - 1) {
            currentGraph = graphList.getLast();
            showGraph(new DrawableGraph(currentGraph));
        }
        scroll.setHvalue(0);
    }
}
