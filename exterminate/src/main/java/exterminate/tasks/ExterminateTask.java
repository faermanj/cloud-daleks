package exterminate.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import exterminate.config.ExterminateConfig;
import exterminate.providers.aws.AWSSeek;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import scar.seek.Seeker;
import scar.seek.Seekers;
import scar.seek.SeekConfig;
import scar.seek.SeekContext;

@Dependent
public class ExterminateTask implements Callable<Void> {
    private static final int LET_USER_THINK = 5;

    @Inject
    Seekers seekers;
    private AtomicBoolean running;

    public void setRunningFlag(AtomicBoolean running) {
        this.running = running;
    }

    @Override
    public Void call() throws Exception {
        present();
        exterminate();
        // Signal display to stop when done
        if (running != null) running.set(false);
        return null;
    }

    private void present() {
        // print each configuration property
        var config = seekers.getConfig(); 
        Log.infof("Configuration:\n%s", config.toJSONString());
        
        Log.info("Registered seekers: ");
        seekers.getInstances()
            .stream()
            .sorted((s1, s2) -> s1.getClass().getName().compareTo(s2.getClass().getName()))
            .forEach(seeker -> Log.info(" - " + seeker.getClass().getSimpleName()));
        seekers.throttle(LET_USER_THINK);
    }

    private void exterminate() {
        seek();
        //TODO: Classify
        //TODO: Act
        //TODO: Report
    }



    private void seek() {
        seekers.seek(AWSSeek.CONTEXT);
    }


}
