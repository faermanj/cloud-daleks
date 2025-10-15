package exterminate.model;

public class CloudResource {
    private final CloudResource parent;
    private final String provider;
    private final String serviceName;
    private final String resourceType;
    private final String name;
    private final String resourceId;

    public CloudResource(String provider, String serviceName, String resourceType, String name) {
        this(null, provider, serviceName, resourceType, name, name);
    }

    public CloudResource(CloudResource parent, String provider, String serviceName, String resourceType, String name,
            String resourceId) {
        this.parent = parent;
        this.provider = provider;
        this.serviceName = serviceName;
        this.resourceType = resourceType;
        this.name = name;
        this.resourceId = resourceId;
    }

    public String getProvider() {
        return provider;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getName() {
        return name;
    }

    public CloudResource getParent() {
        return parent;
    }

    public String getResourceId() {
        return resourceId;
    }

    @Override
    public String toString() {
        return String.format("CloudResource{parent='%s', provider='%s', service='%s', type='%s', name='%s', id='%s'}",
                parent != null ? parent.getName() : "null", provider, serviceName, resourceType, name, resourceId);
    }
}