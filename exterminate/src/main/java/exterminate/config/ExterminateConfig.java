
package exterminate.config;


import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "exterminate")
public interface ExterminateConfig {
    public String noop();
}
