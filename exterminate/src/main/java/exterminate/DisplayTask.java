
package exterminate;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;

@Dependent
public class DisplayTask implements Callable<Void> {
    @Inject
    Execution execution;

    private int intervalSeconds = 5;

    @Override
    public Void call() {
        while (true) {
            clearScreen();
            render();
            try {
                Thread.sleep(intervalSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }

    private void render() {
        displayCloudResources();
    }

    private void displayCloudResources() {
        var resources = execution.getCloudResources();

        System.out.println("Cloud Resources Found:");
        System.out.println("======================");

        if (resources.isEmpty()) {
            System.out.println("No resources found yet...");
        } else {
            // Print table header
            System.out.printf("%-20s %-10s %-15s %-15s %-30s%n",
                    "PARENT", "PROVIDER", "SERVICE", "TYPE", "RESOURCE_ID");
            System.out.println("-".repeat(95));

            // Print table rows
            for (var resource : resources) {
                var parentName = resource.getParent() != null ? resource.getParent().getName() : "-";
                System.out.printf("%-20s %-10s %-15s %-15s %-30s%n",
                        parentName,
                        resource.getProvider(),
                        resource.getServiceName(),
                        resource.getResourceType(),
                        resource.getResourceId());
            }
        }

        System.out.printf("%nTotal resources: %d%n", resources.size());
    }

    private void clearScreen() {
        try {
            // Try to use system-specific clear command
            var os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix/Linux/Mac systems
                System.out.print("\033[2J\033[H");
                System.out.flush();

                // Alternative: try using the clear command
                // new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Fallback: print multiple newlines to simulate clearing
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
