package exterminate.providers.aws.ec2;

import java.util.ArrayList;
import java.util.List;

import exterminate.Execution;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import seek.Seek;
import seek.SeekContext;
import seek.ContextSeeker;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsRequest;

@Dependent
@Seek(name = "provider", value = "aws")
@Seek(name = "service", value = "ec2")
@Seek(name = "resourceType", value = "region")
public class RegionSeek extends ContextSeeker {


    @Override
    public List<SeekContext> seek(SeekContext context) {
        List<SeekContext> result = new ArrayList<>();
        try (var ec2Client = Ec2Client.create()) {
            var request = DescribeRegionsRequest.builder().build();

            var response = ec2Client.describeRegions(request);

            for (var region : response.regions()) {
                var regionContext = context.with(
                        "region", region.regionName());
                result.add(regionContext);
            }
        }
        return result;
    }

}
