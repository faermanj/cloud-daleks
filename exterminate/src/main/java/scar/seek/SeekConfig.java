package scar.seek;

import java.util.Map;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "seek")
public interface SeekConfig {
    Map<String, Map<String, String>> exclude();
    Map<String, Map<String, String>> include();
}
