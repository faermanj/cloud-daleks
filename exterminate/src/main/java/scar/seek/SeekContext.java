package scar.seek;

import scar.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeekContext extends Context {

    private SeekContext(Map<String, String> context) {
        super(context);
    }

    public static final SeekContext of(String key, String value) {
        return new SeekContext(Map.of(key, value));
    }


    public List<SeekContext> with(Map<String, String> contextMap) {
        var union = new HashMap<String, String>();
        union.putAll(getContextMap());
        union.putAll(contextMap);
        return List.of(new SeekContext(union));
    }

    public List<SeekContext> with(String k1, String v1) {
        return with(Map.of(k1, v1));
    }

    public List<SeekContext> with(String k1, String v1, String k2, String v2) {
        return with(Map.of(k1, v1, k2, v2));
    }


}
