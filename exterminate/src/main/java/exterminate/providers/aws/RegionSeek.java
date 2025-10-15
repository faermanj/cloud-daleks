package exterminate.providers.aws;

import exterminate.Execution;
import exterminate.model.CloudResource;
import exterminate.model.Seek;
import exterminate.model.SeekFor;
import exterminate.model.Seeker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsRequest;

@Dependent
@SeekFor(provider = "AWS", service = "Regions", resourceType = "Region")
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
                var resource = new CloudResource(
                        "AWS",
                        "Regions",
                        "Region",
                        region.regionName());
                execution.addCloudResource(resource);
                execution.seek(resource, "AWS", "CloudFormation", "Stack");
            }
        } catch (Exception e) {
            System.err.println("Error listing regions: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        SeekFor annotation = this.getClass().getAnnotation(SeekFor.class);
        if (annotation != null) {
            return String.format("SeekFor{provider='%s', service='%s', resourceType='%s'}",
                    annotation.provider(), annotation.service(), annotation.resourceType());
        }
        return super.toString();
    }
}
