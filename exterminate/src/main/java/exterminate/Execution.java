package exterminate;

import exterminate.config.ExterminateConfig;
import exterminate.model.CloudResource;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import seek.Seeker;
import seek.SeekContext;
import seek.Seek;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class Execution {
    @Inject
    ExterminateConfig config;

    @Inject
    @Any // TODO: Specific inject
    Instance<Seeker> seekerInstance;

    private final List<CloudResource> cloudResources = new CopyOnWriteArrayList<>();

    public void addCloudResource(CloudResource resource) {
        cloudResources.add(resource);
    }

    public List<CloudResource> getCloudResources() {
        return List.copyOf(cloudResources);
    }

    public void clearCloudResources() {
        cloudResources.clear();
    }

    public void seek(SeekContext seekContext) {
        Log.infof("Seeking [%s]", seekContext);
        List<Seeker> seekers = seekerInstance.stream().toList();
        int total = seekers.size();
        seekers = seekers.stream()
                .filter((s) -> matchesSeeker(s, seekContext))
                .toList();
        int matched = seekers.size();
        Log.infof("SeekContext [%s] matched [%s/%s] seekers: %s", seekContext, matched, total, seekers);
        throttle();
        seekers.forEach((s) -> run(s, seekContext));
    }

    private void throttle() {
        try {
            Thread.sleep(60_000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void run(Seeker seeker, SeekContext seekContext) {
        Log.infof("Running seeker [%s] with context [%s]", seeker.getClass().getSimpleName(), seekContext);
        var found = seeker.seek(seekContext);
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
        for (var entry : context.entrySet()) {
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
