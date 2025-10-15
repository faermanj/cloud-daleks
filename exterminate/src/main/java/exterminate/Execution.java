package exterminate;

import exterminate.config.ExterminateConfig;
import exterminate.model.CloudResource;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import seek.Seek;
import seek.SeekContext;
import seek.SeekTarget;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class Execution {
    @Inject
    ExterminateConfig config;

    @Inject
    @Any // TODO: Specific inject
    Instance<Seek> seekerInstance;

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
        List<Seek> seekers = seekerInstance.stream()
                .filter((s) -> matchesSeeker(s, seekContext))
                .toList();
        Log.infof("Matched [%s] seekers", seekers.size());
        seekers.forEach((s) -> run(s, seekContext));
    }

    private void run(Seek seeker, SeekContext seekContext) {
        Log.infof("Running seeker [%s] with context [%s]", seeker.getClass().getSimpleName(), seekContext);
        seeker.setContext(seekContext);
        seeker.run();
    }

    private boolean matchesSeeker(Seek seeker, SeekContext seekContext) {
        var context = seekContext.getContextMap();
        Log.infof("Matching seek [%s] context [%s]", seeker.getClass().getSimpleName(), context);

        // Fetch all SeekTarget annotations from the seeker class
        var seekTargets = seeker.getClass().getAnnotationsByType(SeekTarget.class);

        // Convert SeekTarget annotations to a map for easier lookup
        var seekerAttributes = new java.util.HashMap<String, String>();
        for (var target : seekTargets) {
            seekerAttributes.put(target.name(), target.value());
        }

        Log.infof("Seeker attributes: %s", seekerAttributes);

        // Check if all entries in the context map match the seeker's attributes
        var matches = context.entrySet().stream()
                .allMatch(entry -> {
                    var seekerValue = seekerAttributes.get(entry.getKey());
                    var contextValue = entry.getValue();
                    var isMatch = seekerValue != null && seekerValue.equalsIgnoreCase(contextValue);
                    Log.infof("Checking %s: seeker=%s, context=%s, match=%s",
                            entry.getKey(), seekerValue, contextValue, isMatch);
                    return isMatch;
                });

        Log.infof("Overall match result: %s", matches);
        return matches;
    }

}
