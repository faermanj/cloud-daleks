
package scar.seek;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Main service for running seek operations and managing seeker beans.
 * <p>
 * This class coordinates the seeking process, event firing, and context matching.
 */
@ApplicationScoped
public class Seekers {
    LocalDateTime creationTime = LocalDateTime.now();
    List<SeekContext> seekHistory = new ArrayList<>();
    

    /** Configuration for seek inclusion/exclusion. */
    @Inject
    private SeekConfig seekConfig;

    /** All available seeker beans. */
    @Inject
    @Any
    private Instance<Seeker> seekerInstance;

    /**
     * Checks if the given seek context is excluded by configuration.
     * @param seekContext the context to check
     * @return true if excluded, false otherwise
     */
    public boolean isExcluded(final SeekContext seekContext) {
        var excludeMap = seekConfig.exclude();
        for (var entry : excludeMap.entrySet()) {
            var contextMap = entry.getValue();
            if (contextMap.equals(seekContext.getContextMap())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the given seek context is explicitly included by configuration.
     * @param seekContext the context to check
     * @return true if included, false otherwise
     */
    public boolean isIncluded(final SeekContext seekContext) {
        var includesMap = seekConfig.include();
        for (var entry : includesMap.entrySet()) {
            var configMap = entry.getValue();
            if (equals(configMap, seekContext.getContextMap())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Compares two context maps for equality.
     * @param configMap configuration context map
     * @param seekMap seek context map
     * @return true if maps are equal, false otherwise
     */
    private boolean equals(final Map<String, String> configMap, final Map<String, String> seekMap) {
        return configMap.equals(seekMap);
    }


    /**
     * Runs the seek process for the given context, firing events and delegating to matching seekers.
     * @param seekContext the context to seek
     */
    public void seek(final SeekContext seekContext) {
        Log.tracef("Seeking [%s]", seekContext);
        if (isExcluded(seekContext)) {
            Log.tracef("  Seek context excluded [%s]", seekContext);
            if (!isIncluded(seekContext)) {
                Log.tracef("  Seek context not included, ignoring. [%s]", seekContext);
                return;
            } else {
                Log.tracef("  Seek context re-included. [%s]", seekContext);
            }
        }
        var seekers = seekerInstance.stream().toList();
        int total = seekers.size();
        seekers = seekers.stream()
                .filter(s -> matchesSeeker(s, seekContext))
                .toList();
        int matched = seekers.size();
        Log.infof("Seek context[%s] matched[%s/%s] seekers: %s", seekContext, matched, total, seekers);
        throttle();
        seekers.forEach(s -> fire(s, seekContext));
    }


    /**
     * Throttles execution according to configuration.
     */
    public void throttle() {
        throttle(1);
    }


    /**
     * Runs a seeker for the given context, fires a seek event, and processes all continuations.
     * @param seeker the seeker to run
     * @param seekContext the context to seek
     */
    private void fire(final Seeker seeker, final SeekContext seekContext) {
        Log.infof("Running seeker [%s] with context [%s]", seeker.toString(), seekContext);
        // Create SeekEvent and fire it via CDI Event system
        // This allows other beans to observe seek events and react accordingly.
        // Collect continuations from all event observers and run seek on them
        var event = SeekEvent.of(seekContext);
        //TODO: Fire through CDI events seekEvent.fire(event);
        seeker.onSeek(event);
        var continuations = event.getContinuations();
        Log.infof("Seeker [%s] produced [%d] continuations for context [%s]", seeker.getClass().getSimpleName(), continuations.size(), seekContext);
        for (var continuation : continuations) {
            seek(continuation);
        }
    }


    /**
     * Checks if the seeker matches the given context based on Seek annotations.
     * @param seeker the seeker to check
     * @param seekContext the context to match
     * @return true if matches, false otherwise
     */
    private boolean matchesSeeker(final Seeker seeker, final SeekContext seekContext) {
        var context = new TreeMap<>(seekContext.getContextMap());
        context.entrySet().removeIf(entry -> entry.getKey().startsWith("__"));

        Log.tracef("Matching seek [%s] context [%s]", seeker.getClass().getSimpleName(), context);

        // Fetch all SeekTarget annotations from the seeker class
        var seekTargets = seeker.getClass().getAnnotationsByType(Seek.class);

        // Convert SeekTarget annotations to a map for easier lookup
        var seekerAttributes = new TreeMap<String, String>();
        for (var target : seekTargets) {
            seekerAttributes.put(target.name(), target.value());
        }

        Log.tracef("  Seeker attributes: %s", seekerAttributes);

        // Iterate on seek context and match attributes
        for (var entry : seekContext.getContextMap().entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            var seekerValue = seekerAttributes.get(key);
            var match = (seekerValue != null 
                && (seekerValue.equals("*") ||
                    seekerValue.equalsIgnoreCase(value)));
            if (match) {
                seekerAttributes.remove(key);
                context.remove(key);
            }
            Log.tracef("  Matching context key [%s] value [%s] with seeker value [%s]: %s", key, value, seekerValue, match);
        }
        var result = seekerAttributes.isEmpty() && context.isEmpty();
        Log.tracef("  Overall match result: %s", result);
        return result;
    }


    public SeekConfig getConfig() {
        return seekConfig;
    }


    public List<Seeker> getInstances() {
        var seekers = seekerInstance.stream().toList();
        return seekers;
     }


    public void throttle(int multiplier) {
        try {
            Thread.sleep(1_000 * seekConfig.throttle() * multiplier);
        } catch (InterruptedException e) {
            Log.error("Throttle interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

}
