package scar.seek;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class ContinuationsSeeker implements Seeker {
    List<SeekContext> continuations = new ArrayList<>();
    SeekContext context;

    public List<SeekContext> seek(SeekContext context) {
        this.context = context;
        continuations.clear();
        onSeek();
        return continuations;
    }

    protected void onSeek() {
    }

    protected void addall(List<SeekContext> continuations) {
        this.continuations.addAll(continuations);
    }

    protected SeekContext getContext() {
        return context;
    }

    @Override
    public String toString() {
        return toJSON();
    }

    protected void add(List<SeekContext> continuations) {
        addall(continuations);
    }

    protected void add(String k1, String v1, String k2, String v2) {
        var continuations = context.with(k1, v1, k2, v2);
        addall(continuations);
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
