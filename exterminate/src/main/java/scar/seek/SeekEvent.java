package scar.seek;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeekEvent {
    // The context for which this event was fired
    private final SeekContext seekContext;

    // List to collect continuations from observers
    private final List<SeekContext> continuations = new ArrayList<>();

    /**
     * Constructs a SeekEvent for the given context.
     * @param seekContext the context for this event
     */
    public SeekEvent(final SeekContext seekContext) {
        this.seekContext = seekContext;
    }

    /**
     * Static factory method for cleaner instantiation.
     */
    public static SeekEvent of(final SeekContext seekContext) {
        return new SeekEvent(seekContext);
    }

    /**
     * Adds a list of continuations produced by observers.
     */
    public void addContinuations(final List<SeekContext> contexts) {
        continuations.addAll(contexts);
    }

    /**
     * Returns all collected continuations.
     */
    public List<SeekContext> getContinuations() {
        return continuations;
    }

    /**
     * Returns the context for which this event was fired.
     */
    public SeekContext getSeekContext() {
        return seekContext;
    }

    public List<SeekContext> with(String k, String v) {
        return seekContext.with(k,v);
    }
}
