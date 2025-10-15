package exterminate;

import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import exterminate.tasks.ExterminateTask;

@QuarkusMain
@TopCommand
@CommandLine.Command(name = "exterminate", mixinStandardHelpOptions = true)
public class ExterminateMain implements QuarkusApplication, Runnable {
    @Inject
    CommandLine.IFactory factory; 

    @Inject
    Execution execution;

    @Inject
    ExterminateTask exterminateTask;

    @Inject
    DisplayTask displayTask;

    @Override
    public void run() {        
        Log.info("EXTERMINATE! FINAL WARNING: Daleks are *destructive*...");
        exterminate();
        Log.info("EXTERMINATE! daleks finished! " + execution);
    }

    private void exterminate() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Create and start the timer task
            var timerFuture = executor.submit(exterminateTask);

            // Create and start the display task
            // var displayFuture = executor.submit(displayTask);

            // Wait for both tasks to complete
            timerFuture.get();
            // displayFuture.get();
            
            Log.info("All tasks completed successfully!");
            Log.info("Final execution state: " + execution);
            
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
