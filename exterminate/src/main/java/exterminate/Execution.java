package exterminate;

import exterminate.config.ExterminateConfig;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import scar.seek.Seeker;
import scar.seek.SeekContext;
import scar.seek.Seek;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Execution {
    @Inject
    ExterminateConfig config;

    @Inject
    @Any
    Instance<Seeker> seekerInstance;


    public boolean isExcluded(SeekContext seekContext){
        // Check against configuration
        var excludeMap = config.seekExclude();
        for (var entry : excludeMap.entrySet()) {
            var contextMap = entry.getValue();
            if (contextMap.equals(seekContext.getContextMap())) {
                return true;
            }
        }
        return false;
    }

    public boolean isIncluded(SeekContext seekContext){
        // Check against configuration
        var includesMap = config.seekInclude();
        for (var entry : includesMap.entrySet()) {
            var configMap = entry.getValue();
            var isIncluded = matches(configMap, seekContext.getContextMap());
            if(isIncluded){
                return true;
            }
        }
        return false;
    }

    private boolean matches(Map<String, String> configMap, Map<String, String> seekMap) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'matches'");
    }

    public void seek(SeekContext seekContext) {
        Log.infof("Seeking [%s]", seekContext);
        if (isExcluded(seekContext)){
            Log.infof("  Seek context excluded [%s]", seekContext);
            if (! isIncluded(seekContext)){
                Log.infof("  Seek context not included, ignoring. [%s]", seekContext);
                return;
            }else{
                Log.infof("  Seek context re-included. [%s]", seekContext);
            }
        }
        List<Seeker> seekers = seekerInstance.stream().toList();
        int total = seekers.size();
        seekers = seekers.stream()
                .filter((s) -> matchesSeeker(s, seekContext))
                .toList();
        int matched = seekers.size();
        Log.infof("SeekContext [%s] matched [%s/%s] seekers: %s", seekContext, matched, total, seekers);
        throttle();
        seekers.forEach((s) -> seek(s, seekContext));
    }

    public void throttle() {
        try {
            Thread.sleep(config.throttle());
        } catch (InterruptedException e) {
            Log.error("Throttle interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private void seek(Seeker seeker, SeekContext seekContext) {
        Log.infof("Running seeker [%s] with context [%s]", seeker.getClass().getSimpleName(), seekContext);
        var found = seeker.seek(seekContext);
        Log.infof("   Seeker [%s] found [%s] continuations: %s", seeker.getClass().getSimpleName(), found.size(), found);
        found.forEach(this::seek);
    }

    private boolean matchesSeeker(Seeker seeker, SeekContext seekContext) {
        var context = new HashMap<>(seekContext.getContextMap());
        Log.tracef("Matching seek [%s] context [%s]", seeker.getClass().getSimpleName(), context);

        // Fetch all SeekTarget annotations from the seeker class
        var seekTargets = seeker.getClass().getAnnotationsByType(Seek.class);

        // Convert SeekTarget annotations to a map for easier lookup
        var seekerAttributes = new HashMap<String, String>();
        for (var target : seekTargets) {
            seekerAttributes.put(target.name(), target.value());
        }

        Log.tracef("  Seeker attributes: %s", seekerAttributes);

        // iterate on seekcontext
        for (var entry : seekContext.getContextMap().entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var seekerValue = seekerAttributes.get(key);
            var match = (seekerValue != null && seekerValue.equalsIgnoreCase(value));
            if (match){
                seekerAttributes.remove(key);
                context.remove(key);
            }
            Log.tracef("  Matching context key [%s] value [%s] with seeker value [%s]: %s", key, value, seekerValue, match);
        }
        var result = seekerAttributes.isEmpty() && context.isEmpty();
        Log.tracef("  Overall match result: %s", result);
        return result;
    }

}
