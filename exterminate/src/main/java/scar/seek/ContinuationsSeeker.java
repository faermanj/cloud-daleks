package scar.seek;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.quarkus.logging.Log;
import jakarta.enterprise.event.Observes;

public abstract class ContinuationsSeeker implements Seeker {

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

    public void onSeek(@Observes SeekEvent event) {
        Log.warn("Empty onSeek() listener.");
    }


}

