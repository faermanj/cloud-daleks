package seek;

import java.util.List;
import seek.Seek;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

public abstract class ContextSeeker implements Seeker {
    SeekContext context;

    public SeekContext getContext() {
        return context;
    }

    public void setContext(SeekContext context) {
        this.context = context;
    }

    public List<SeekContext> seek(SeekContext context) {
        return List.of(context);
    }

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
        java.util.Map<String, String> seekMap = new java.util.TreeMap<>();
        for (Seek seek : seeks) {
            seekMap.put(seek.name(), seek.value());
        }
        for (var entry : seekMap.entrySet()) {
            sb.append(", \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

}
