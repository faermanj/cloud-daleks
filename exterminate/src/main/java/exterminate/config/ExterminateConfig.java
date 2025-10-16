package exterminate.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "exterminate")
public interface ExterminateConfig {
    @WithDefault("uala")
    String uala();

    @WithDefault("10000")
    Integer throttle();

}
