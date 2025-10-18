package scar.seek;

import java.util.Map;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "seek")
@StaticInitSafe
public interface SeekConfig  {
    Map<String, Map<String, String>> exclude();
    Map<String, Map<String, String>> include();
}
