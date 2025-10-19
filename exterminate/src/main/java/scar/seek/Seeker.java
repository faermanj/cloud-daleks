package scar.seek;

import jakarta.enterprise.event.Observes;

public interface Seeker  {
    void onSeek(@Observes SeekEvent event); 
}
