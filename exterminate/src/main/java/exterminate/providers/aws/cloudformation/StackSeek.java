package exterminate.providers.aws.cloudformation;

import exterminate.Execution;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import seek.Seek;
import seek.SeekTarget;
import seek.SeekTargets;
import seek.Seeker;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.ListStacksRequest;

@Dependent
@SeekTargets({
    @SeekTarget(name="provider", value = "aws"),
    @SeekTarget(name="service", value = "cloudformation"),
    @SeekTarget(name="resourceType", value = "stack")
})
public class StackSeek extends Seeker{

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
                Log.infof("TODO: add stack to index");
            }
        } catch (Exception e) {
            System.err.println("Error listing stacks: " + e.getMessage());
        }
    }


}
