
package exterminate.providers.aws;

import static exterminate.scar.SeekSymbols.PROVIDER;

import io.quarkus.logging.Log;

import static exterminate.scar.SeekSymbols.AWS;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import scar.seek.ContinuationsSeeker;
import scar.seek.Seek;
import scar.seek.SeekEvent;

/**
 * AWSSeek is a CDI bean that observes {@link SeekEvent} and contributes AWS-specific continuations.
 * <p>
 * This class is registered for the AWS provider and adds EC2 region continuations when a seek event is observed.
 */
@Dependent
@Seek(name = PROVIDER, value = AWS)
public class AWSSeek extends ContinuationsSeeker {

    /**
     * Observes {@link SeekEvent} and adds AWS EC2 region continuations.
     *
     * @param event the seek event containing the context for which continuations are requested
     */
    @Override
    public void onSeek(@Observes SeekEvent event) {
        // Adds AWS EC2 region continuation to the event
        event.continueWith("service", "ec2",
            "resourceType", "region");
        // If needed, use event.getSeekContext() for context-specific logic
        var count = event.getContinuations().size();
        Log.infof("AWSSeek added [%d] continuations.", count);
    }
}

