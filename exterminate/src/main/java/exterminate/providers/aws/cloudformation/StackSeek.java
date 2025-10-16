package exterminate.providers.aws.cloudformation;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import scar.seek.Seek;
import scar.seek.ContextSeeker;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.ListStacksRequest;

@Dependent
@Seek(name = "provider", value = "aws")
@Seek(name = "service", value = "cloudformation")
@Seek(name = "resourceType", value = "stack")
public class StackSeek extends ContextSeeker {



    public void listAllStacks() {
        try (var cfClient = CloudFormationClient.create()) {
            var request = ListStacksRequest.builder().build();

            var response = cfClient.listStacks(request);

            for (var stack : response.stackSummaries()) {
                Log.infof("TODO: add stack to index");
            }
        } catch (Exception e) {
            System.err.println("Error listing stacks: " + e.getMessage());
        }
    }


}
