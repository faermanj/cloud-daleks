package seek;

import io.quarkus.logging.Log;

public class Seeker implements Seek {
    SeekContext context;

    public SeekContext getContext() {
        return context;
    }

    public void setContext(SeekContext context) {
        this.context = context;
    }
    
    @Override
    public void run() {
        Log.info("SEEK RUN %s".formatted(context));
    }

}
