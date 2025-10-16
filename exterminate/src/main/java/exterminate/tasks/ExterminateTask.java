package exterminate.tasks;

import java.util.concurrent.Callable;

import exterminate.Execution;
import exterminate.config.ExterminateConfig;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import scar.seek.Seeker;
import scar.seek.SeekContext;

@Dependent
public class ExterminateTask implements Callable<Void> {
    @Inject
    Execution execution;

    @Inject
    ExterminateConfig config;

    @Inject
    Instance<Seeker> seekers;

    @Override
    public Void call() throws Exception {
        present();
        exterminate();
        Thread.sleep(5 * 60 * 1000); // Sleep for 5 minutes
        return null;
    }

    private void present() {
        // print each configuration property
        Log.info("Configuration:");
        Log.info(" - Throttle: " + config.throttle() + " ms");
        Log.info(" - Seek Exclude: " + config.seekExclude());
        Log.info(" - Seek Include: " + config.seekInclude());
        Log.info("Registered seekers: ");
        for (Seeker seeker : seekers) {
            Log.info(" - " + seeker.getClass().getName());
        }
        execution.throttle();
    }

    private void exterminate() {
        seek();
        destroy();
        Log.info("Exterminate!");
    }

    private void destroy() {
        Log.warn("EXTERMINATE!");
    }

    private void seek() {
        Log.info("Starting resource discovery...");

        // Seek for AWS regions specifically
        execution.seek(SeekContext.of("provider", "aws"));



        Log.info("Resource discovery completed");
    }


}
