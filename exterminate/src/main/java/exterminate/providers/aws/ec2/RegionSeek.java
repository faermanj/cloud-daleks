package exterminate.providers.aws.ec2;

import static exterminate.scar.SeekSymbols.AWS;
import static exterminate.scar.SeekSymbols.EC2;
import static exterminate.scar.SeekSymbols.PROVIDER;
import static exterminate.scar.SeekSymbols.REGION;
import static exterminate.scar.SeekSymbols.RESOURCE_TYPE;
import static exterminate.scar.SeekSymbols.SERVICE;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import scar.seek.ContinuationsSeeker;
import scar.seek.Seek;
import scar.seek.SeekEvent;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsRequest;

@Dependent
@Seek(name = PROVIDER, value = AWS)
@Seek(name = SERVICE, value = EC2)
@Seek(name = RESOURCE_TYPE, value = REGION)
public class RegionSeek extends ContinuationsSeeker {
    public void onSeek(final @Observes SeekEvent event) {
        try (var ec2Client = Ec2Client.create()) {
            var request = DescribeRegionsRequest.builder().build();
            var response = ec2Client.describeRegions(request);
            for (var region : response.regions()) {
                continueWith(event, "region", region.regionName());
            }
        }
    }

}
