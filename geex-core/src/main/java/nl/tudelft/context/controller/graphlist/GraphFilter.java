package nl.tudelft.context.controller.graphlist;

import nl.tudelft.context.model.graph.filter.CodingSequenceFilter;
import nl.tudelft.context.model.graph.filter.ResistanceCausingMutationFilter;
import nl.tudelft.context.model.graph.filter.BaseLengthFilter;
import nl.tudelft.context.model.graph.filter.StraightFilter;
import nl.tudelft.context.model.graph.filter.InsertDeleteFilter;
import nl.tudelft.context.model.graph.filter.SinglePointFilter;
import nl.tudelft.context.model.graph.filter.StackGraphFilter;
import nl.tudelft.context.model.graph.filter.UnknownFilter;

/**
 * List the available graph filters.
 *
 * @author Gerben Oolbekkink
 * @version 1.0
 * @since 18-6-2015
 */
public enum GraphFilter {
    /**
     * Class for filtering a Single Point mutation graph.
     */
    SINGLE_POINT(SinglePointFilter.class, "Point mutations"),
    /**
     * Class for filtering indels.
     */
    INSERT_DELETE(InsertDeleteFilter.class, "Insertions / Deletions"),
    /**
     * Class for collapsing straight lines.
     */
    COLLAPSE(StraightFilter.class, "Straight sequences"),
    /**
     * Class for hiding nodes with too many unknowns.
     */
    UNKNOWN(UnknownFilter.class, "Unknown bases (" + (UnknownFilter.REMOVE_RATIO * 100) + "%)"),
    /**
     * Class for hiding nodes that don't contain coding sequences.
     */
    CODING_SEQUENCE(CodingSequenceFilter.class, "Coding sequences"),
    /**
     * Class for hiding nodes that don't contain resistance causing mutations.
     */
    RESISTANCE_CAUSING(ResistanceCausingMutationFilter.class, "Drug resistance"),
    /**
     * Class for hiding nodes which are too short.
     */
    BASE_LENGTH(BaseLengthFilter.class, "Base length (" + BaseLengthFilter.THRESHOLD + ")");

    /**
     * Class with filter.
     */
    private final Class<? extends StackGraphFilter> graph;
    /**
     * Name of this filter.
     */
    private final String name;

    /**
     * Create a new GraphFilterEnum, this enum is used for creating filters on the graph.
     *
     * @param graph Class of the filter.
     * @param name  Name of the filter.
     */
    GraphFilter(final Class<? extends StackGraphFilter> graph, final String name) {
        this.graph = graph;
        this.name = name;
    }

    /**
     * Get the filter for this enum.
     *
     * @return The filter
     */
    public final Class<? extends StackGraphFilter> getGraph() {
        return graph;
    }

    @Override
    public final String toString() {
        return name;
    }
}
