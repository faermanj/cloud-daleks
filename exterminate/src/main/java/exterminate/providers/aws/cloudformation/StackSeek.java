package exterminate.providers.aws.cloudformation;

import exterminate.Execution;
import exterminate.model.CloudResource;
import exterminate.model.Seek;
import exterminate.model.SeekFor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.ListStacksRequest;

@Dependent
@SeekFor(provider = "AWS", service = "CloudFormation", resourceType = "Stack")
public class StackSeek implements Seek {

    @Inject
    Execution execution;

    @Override
    public void run() {
        listAllStacks();
    }

    public void listAllStacks() {
        try (var cfClient = CloudFormationClient.create()) {
            var request = ListStacksRequest.builder().build();

            var response = cfClient.listStacks(request);

            for (var stack : response.stackSummaries()) {
                var resource = new CloudResource("AWS", "CloudFormation", "Stack", stack.stackName());
                execution.addCloudResource(resource);
            }
        } catch (Exception e) {
            System.err.println("Error listing stacks: " + e.getMessage());
        }
    }

    @Override
    public void setParent(CloudResource parent) {
        // Not used in this implementation
    }

    @Override
    public void setProvider(String provider) {
        // Not used in this implementation
    }

    @Override
    public void setService(String service) {
        // Not used in this implementation
    }

    @Override
    public void setResourceType(String resourceType) {
        // Not used in this implementation
    }
}
