package exterminate.model;

import io.quarkus.logging.Log;

public class Seeker implements Seek {
    CloudResource parent;
    String provider;
    String service;
    String resourceType;
    
    @Override
    public void run() {
        Log.info("SEEK %s %s %s %s".formatted(parent, provider, service, resourceType));
    }

    @Override
    public void setParent(CloudResource parent) {
        this.parent = parent;
    }

    @Override
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public void setService(String service) {
        this.service = service;
    }
    
    @Override
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public CloudResource getParent() {
        return parent;
    }
    
    public String getProvider() {
        return provider;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public String getService() {
        return service;
    }
}
