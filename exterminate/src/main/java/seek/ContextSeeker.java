package seek;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import seek.Seek;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;

public abstract class ContextSeeker implements Seeker {


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

}
