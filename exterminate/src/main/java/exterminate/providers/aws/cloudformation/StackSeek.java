package exterminate.providers.aws.cloudformation;

import static exterminate.scar.SeekSymbols.*;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import scar.seek.Seek;
import scar.seek.SeekEvent;
import scar.seek.ContinuationsSeeker;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.ListStacksRequest;

@Dependent
@Seek(name = PROVIDER, value = AWS)
@Seek(name = SERVICE, value = CLOUDFORMATION)
@Seek(name = RESOURCE_TYPE, value = STACK)
public class StackSeek extends ContinuationsSeeker {

    @Override
    protected void onSeek(@Observes SeekEvent event) {
        try (var cfClient = CloudFormationClient.create()) {
            var request = ListStacksRequest.builder().build();

            var response = cfClient.listStacks(request);

            for (var stack : response.stackSummaries()) {
                var stackId = stack.stackId();
                continueWith(event, "stackId", stackId);
            }
        } catch (Exception e) {
            System.err.println("Error listing stacks: " + e.getMessage());
        }
    }


}
