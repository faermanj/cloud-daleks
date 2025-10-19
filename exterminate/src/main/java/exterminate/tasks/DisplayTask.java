
package exterminate.tasks;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.Dependent;

@Dependent
public class DisplayTask implements Callable<Void> {
    private AtomicBoolean running = new AtomicBoolean(true);

    public void setRunningFlag(AtomicBoolean running) {
        this.running = running;
    }

    @Override
    public Void call() {
        long startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> running.set(false)));
        try {
            while (running.get()) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                long seconds = elapsedMillis / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                System.out.printf("\r[Elapsed: %02d:%02d]", minutes, seconds);
                System.out.flush();
                Thread.sleep(2345L);
            }
            System.out.println("\nExterminateTask finished.");
        } catch (InterruptedException e) {
            running.set(false);
            Thread.currentThread().interrupt();
        }
        return null;
    }
}
