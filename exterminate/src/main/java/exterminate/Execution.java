package exterminate;

import exterminate.config.ExterminateConfig;
import exterminate.model.CloudResource;
import exterminate.model.Seek;
import exterminate.model.SeekFor;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class Execution {
    @Inject
    ExterminateConfig config;

    @Inject @Any //TODO: Specific inject
    Instance<Seek> seekerInstance;

    private final List<CloudResource> cloudResources = new CopyOnWriteArrayList<>();

    public void addCloudResource(CloudResource resource) {
        cloudResources.add(resource);
    }

    public List<CloudResource> getCloudResources() {
        return new ArrayList<>(cloudResources);
    }

    public void clearCloudResources() {
        cloudResources.clear();
    }

    public void seek(CloudResource parent, String provider, String service, String resourceType) {
        Log.infof("Seeking [%s %s %s %s]", parent, provider, service, resourceType);
        seekerInstance.stream()
            .filter((s) -> matchesSeeker(s, provider, service, resourceType))
            .forEach((s) -> run(s, parent, provider, service, resourceType));
    }
    

    private void run(Seek seeker, CloudResource parent, String provider, String service, String resourceType) {
        Log.infof("Running seek [%s %s %s %s]", parent, provider, service, resourceType);
        seeker.setParent(parent);
        seeker.setProvider(provider);
        seeker.setService(service);
        seeker.setResourceType(resourceType);
        seeker.run();
    }

    private boolean matchesSeeker(Seek seeker, String provider, String service, String resourceType) {
        Log.infof("Matching seek [%s] [%s %s %s]", seeker, provider, service, resourceType);
        var seekForAnnotation = seeker.getClass().getAnnotation(SeekFor.class);

        if (seekForAnnotation == null) {
            Log.infof("Seeker [%s] not annotated", seeker);
            return false;
        }
        var matches = seekForAnnotation.provider().equalsIgnoreCase(provider) &&
                seekForAnnotation.service().equalsIgnoreCase(service) &&
                seekForAnnotation.resourceType().equalsIgnoreCase(resourceType);
        Log.infof("Seeker [%s] matches [%s %s %s]: [%s]", seeker, provider, service, resourceType, matches);
        return matches;
    }

}
