package scar;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

import scar.report.ReportUtils;

public class Context {
    Map<String, String> contextMap;

    protected Context(Map<String, String> contextMap){
        this.contextMap =  new TreeMap<>(contextMap);
        var now = LocalDateTime.now();
        var creationTime = ReportUtils.format(now);
        this.contextMap.put("__creationTime", creationTime);
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
