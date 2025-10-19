package exterminate.providers.aws;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import scar.seek.ContinuationsSeeker;
import scar.seek.Seek;
import static scar.seek.Seek.ANY;
import scar.seek.SeekEvent;

import static exterminate.scar.SeekSymbols.*;
import static scar.seek.Seeker.*;

@Dependent
@Seek(name = PROVIDER, value = AWS)
@Seek(name = SERVICE, value = EC2)
@Seek(name = RESOURCE_TYPE, value = REGION)
@Seek(name = REGION, value = ANY)
public class RegionServicesSeek  extends ContinuationsSeeker {
    @Override
    protected void onSeek(@Observes SeekEvent event) {
        continueWith(event,"service", "cloudformation",
            "resourceType", "stack");
    }
}