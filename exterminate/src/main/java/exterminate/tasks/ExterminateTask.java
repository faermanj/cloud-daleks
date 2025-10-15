package exterminate.tasks;

import java.util.concurrent.Callable;

import exterminate.Execution;
import exterminate.model.Seek;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Dependent
public class ExterminateTask implements Callable<Void> {
    @Inject
    Execution execution;

    @Inject
    Instance<Seek> seekers;

    @Override
    public Void call() throws Exception {
        exterminate();
        Thread.sleep(5 * 60 * 1000); // Sleep for 5 minutes
        return null;
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
        execution.seek(null , "AWS", "Regions", "Region");



        Log.info("Resource discovery completed");
    }


}
