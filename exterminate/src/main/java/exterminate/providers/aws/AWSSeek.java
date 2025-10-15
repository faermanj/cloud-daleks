package exterminate.providers.aws;

import seek.SeekContext;
import seek.SeekTarget;
import seek.Seeker;

@SeekTarget(name = "provider", value = "aws")
public class AWSSeek extends Seeker {

    public SeekContext seek(SeekContext context) {
        return context.with(
                "service", "ec2",
                "resourceType", "region");
    }
}
