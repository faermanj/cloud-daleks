package exterminate.model;

public interface Seek extends Runnable {

    void setParent(CloudResource parent);

    void setProvider(String provider);

    void setService(String service);

    void setResourceType(String resourceType);


}