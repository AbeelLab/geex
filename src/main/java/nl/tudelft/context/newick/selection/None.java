package nl.tudelft.context.newick.selection;

/**
 * @author René Vennik <renevennik@gmail.com>
 * @version 1.0
 * @since 22-5-2015
 */
public class None extends Selection {

    @Override
    public boolean useSources() {
        return false;
    }

    @Override
    public Selection toggle() {
        return new All();
    }

    @Override
    public Selection merge(final Selection selection) {
        if (selection instanceof None) {
            return this;
        }
        return new Partial();
    }

    @Override
    public String styleClass() {
        return "none";
    }

}