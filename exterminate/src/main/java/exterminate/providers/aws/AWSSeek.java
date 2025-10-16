package exterminate.providers.aws;

import scar.seek.SeekContext;
import scar.seek.Seek;

import java.util.List;

import jakarta.enterprise.context.Dependent;
import scar.seek.ContextSeeker;

@Dependent
@Seek(name = "provider", value = "aws")
public class AWSSeek extends ContextSeeker {

    @Override
    public List<SeekContext> seek(SeekContext context) {
        return context.with(
                "service", "ec2",
                "resourceType", "region");
    }
}
