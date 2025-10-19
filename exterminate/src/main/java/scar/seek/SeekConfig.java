package scar.seek;

import java.util.Map;
import java.util.function.Function;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "seek")
@StaticInitSafe
public interface SeekConfig  {
    @WithDefault("2")
    Integer throttle();
    Map<String, Map<String, String>> exclude();
    Map<String, Map<String, String>> include();

    default String toJSONString() {
        var sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"throttle\": ").append(throttle()).append(",\n");
        sb.append("  \"exclude\": ").append(toJSONString(exclude())).append(",\n");
        sb.append("  \"include\": ").append(toJSONString(include())).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toJSONString(Map<String, Map<String, String>> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }

        Function<String, String> esc = s -> {
            if (s == null) return "";
            return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
        };

        var sb = new StringBuilder();
        sb.append("{\n");
        var it = map.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .iterator();
        while (it.hasNext()) {
            var e = it.next();
            sb.append("    \"").append(esc.apply(e.getKey())).append("\": ");
            var inner = e.getValue();
            if (inner == null || inner.isEmpty()) {
            sb.append("{}");
            } else {
            sb.append("{\n");
            var it2 = inner.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .iterator();
            while (it2.hasNext()) {
                var e2 = it2.next();
                sb.append("      \"").append(esc.apply(e2.getKey())).append("\": ");
                sb.append("\"").append(esc.apply(e2.getValue())).append("\"");
                if (it2.hasNext()) sb.append(",");
                sb.append("\n");
            }
            sb.append("    }");
            }
            if (it.hasNext()) sb.append(",");
            sb.append("\n");
        }
        sb.append("  }");
        return sb.toString();
    }
}
