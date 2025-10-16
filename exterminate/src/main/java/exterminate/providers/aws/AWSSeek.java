package exterminate.providers.aws;

import static exterminate.scar.SeekSymbols.*;

import jakarta.enterprise.context.Dependent;
import scar.seek.ContinuationsSeeker;
import scar.seek.Seek;

@Dependent
@Seek(name = PROVIDER, value = AWS)
public class AWSSeek extends ContinuationsSeeker {
    @Override
    protected void onSeek() {
        add("service", "ec2",
            "resourceType", "region");
    }
}
