package scar.seek;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.quarkus.logging.Log;
import jakarta.enterprise.event.Observes;

public abstract class ContinuationsSeeker implements Seeker {
    // Fields
    protected final List<SeekContext> continuations = new ArrayList<>();

    // Public methods
    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        var sb = new StringBuilder();
        var clazz = this.getClass();
        sb.append("{");
        sb.append("\"className\": \"").append(clazz.getSimpleName()).append("\"");
        Seek[] seeks = clazz.getAnnotationsByType(Seek.class);
        Map<String, String> seekMap = new TreeMap<>();
        for (Seek seek : seeks) {
            seekMap.put(seek.name(), seek.value());
        }
        for (var entry : seekMap.entrySet()) {
            sb.append(", \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    // Protected methods
    protected void addall(List<SeekContext> continuations) {
        this.continuations.addAll(continuations);
    }

    protected void continueWith(List<SeekContext> continuations) {
        addall(continuations);
    }

    //TODO: Deduplicate code
    protected void continueWith(SeekEvent event, String k1, String v1) {
        var context = event.getSeekContext();
        var continuations = context.with(k1, v1);
        addall(continuations);
    }

    protected void continueWith(SeekEvent event, String k1, String v1, String k2, String v2) {
        var context = event.getSeekContext();
        var continuations = context.with(k1, v1, k2, v2);
        addall(continuations);
    }

    // Package-private methods
    protected void onSeek(@Observes SeekEvent event) {
        Log.warn("Empty onSeek() listener.");
    }
}

