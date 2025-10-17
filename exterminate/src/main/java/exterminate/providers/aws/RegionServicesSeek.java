package exterminate.providers.aws;

import jakarta.enterprise.context.Dependent;
import scar.seek.Seek;

import static exterminate.scar.SeekSymbols.*;
import static exterminate.scar.SeekSymbols.EC2;
import static exterminate.scar.SeekSymbols.REGION;
import static exterminate.scar.SeekSymbols.RESOURCE_TYPE;
import static scar.seek.Seeker.*;

@Dependent
@Seek(name = PROVIDER, value = AWS)
@Seek(name = SERVICE, value = EC2)
@Seek(name = RESOURCE_TYPE, value = REGION)
@Seek(name = REGION, value = ANY)
public class RegionServicesSeek {
}
