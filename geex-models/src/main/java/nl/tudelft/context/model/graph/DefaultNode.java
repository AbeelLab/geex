package nl.tudelft.context.model.graph;

import nl.tudelft.context.model.annotation.AnnotationMap;
import nl.tudelft.context.model.annotation.CodingSequence;
import nl.tudelft.context.model.annotation.Resistance;

import java.util.List;
import java.util.Set;

/**
 * @author René Vennik
 * @version 1.0
 * @since 1-6-2015
 */
public abstract class DefaultNode {

    /**
     * Shift if alone in column.
     */
    boolean shift = false;

    /**
     * Set of genomes that contain this Node.
     */
    Set<String> sources;

    /**
     * The content of the current Node.
     */
    String content;

    /**
     * Get the size in nodes of the node.
     *
     * @return Size in nodes of the node
     */
    public abstract int getSize();

    /**
     * Getter for sources.
     *
     * @return sources
     */
    public Set<String> getSources() {
        return sources;
    }

    /**
     * Getter for content.
     *
     * @return DNA sequence
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for baseCounter.
     *
     * @return baseCounter
     */
    public abstract BaseCounter getBaseCounter();

    /**
     * Getter for reference start position.
     *
     * @return reference start position
     */
    public abstract int getRefStartPosition();

    /**
     * Getter for reference end position.
     *
     * @return reference end position
     */
    public abstract int getRefEndPosition();

    /**
     * Sets the codingSequences that belong to this node.
     *
     * @param codingSequenceMap The list of all codingSequences
     */
    public abstract void setCodingSequences(final AnnotationMap<CodingSequence> codingSequenceMap);

    /**
     * Get the list of codingSequences that are present in this node.
     *
     * @return The list of codingSequences present in this node
     */
    public abstract List<CodingSequence> getCodingSequences();

    /**
     * Sets the resistance mutations that belong to this node.
     *
     * @param resistanceMap The list of all resistance mutations
     */
    public abstract void setResistance(final AnnotationMap<Resistance> resistanceMap);

    /**
     * Get the list of resistance mutations that are present in this node.
     *
     * @return The list of resistance mutations present in this node
     */
    public abstract List<Resistance> getResistance();

    /**
     * Shift the node.
     */
    public void shift() {
        shift = true;
    }

    /**
     * Check if the node is shifted.
     *
     * @return If node is shifted
     */
    public boolean isShift() {
        return shift;
    }

}
