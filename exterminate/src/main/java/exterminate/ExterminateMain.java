package exterminate;

import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;
import scar.seek.Seekers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import exterminate.tasks.DisplayTask;
import exterminate.tasks.ExterminateTask;

@QuarkusMain
@TopCommand
@CommandLine.Command(name = "exterminate", mixinStandardHelpOptions = true)
public class ExterminateMain implements QuarkusApplication, Runnable {
    @Inject
    CommandLine.IFactory factory; 

    @Inject
    Seekers sekers;

    @Inject
    ExterminateTask exterminateTask;

    @Inject
    DisplayTask displayTask;

    @Override
    public void run() {        
        Log.info("EXTERMINATE! FINAL WARNING: Daleks are *destructive*...");
        exterminate();
        Log.info("EXTERMINATE! daleks finished! " + sekers);
    }

    private void exterminate() {
        AtomicBoolean running = new AtomicBoolean(true);
        displayTask.setRunningFlag(running);
        exterminateTask.setRunningFlag(running);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var displayFuture = executor.submit(displayTask);
            var timerFuture = executor.submit(exterminateTask);
            timerFuture.get();
            running.set(false); // signal display to stop
            displayFuture.get();
            Log.info("All tasks completed successfully!");
            Log.info("Final execution state: " + sekers);
        } catch (InterruptedException e) {
            Log.error("Tasks were interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Log.error("Error during execution", e);
        }
    }


    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(this, factory).execute(args);
    }

    public static void main(String[] args) {
        Quarkus.run(ExterminateMain.class, args);
    }
}
