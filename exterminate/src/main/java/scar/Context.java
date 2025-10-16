package scar;

import java.util.Map;

public class Context {
    Map<String, String> contextMap;

    protected Context(Map<String, String> contextMap){
        this.contextMap = contextMap;
    }

    public Map<String,String> getContextMap() {
        return contextMap;
    }

    @Override
    public String toString() {
        return toJSON();
    }

    private String toJSON() {
        if (contextMap == null || contextMap.isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");
        sb.append("\"className\": \"").append(this.getClass().getSimpleName()).append("\",");
        var iter = contextMap.entrySet().iterator();
        while (iter.hasNext()) {
            var e = iter.next();
            sb.append('"').append(e.getKey()).append('"')
                    .append(':')
                    .append('"').append(e.getValue()).append('"');
            if (iter.hasNext()) sb.append(',');
        }
        sb.append('}');
        return sb.toString();
    }
}
