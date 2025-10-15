package exterminate.providers.aws.ec2;

import exterminate.Execution;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import seek.SeekTarget;
import seek.Seeker;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsRequest;

@Dependent

@SeekTarget(name = "provider", value = "aws")
@SeekTarget(name = "service", value = "ec2")
@SeekTarget(name = "resourceType", value = "region")
public class RegionSeek extends Seeker {

    @Inject
    Execution execution;

    @Override
    public void run() {

        listAllRegions();
    }

    public void listAllRegions() {
        try (var ec2Client = Ec2Client.create()) {
            var request = DescribeRegionsRequest.builder().build();

            var response = ec2Client.describeRegions(request);

            for (var region : response.regions()) {
                var context = getContext().with(
                        "region", region.regionName());
                execution.seek(context);
            }
        } catch (Exception e) {
            System.err.println("Error listing regions: " + e.getMessage());
        }
    }
}
