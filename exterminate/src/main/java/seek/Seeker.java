package seek;

import java.util.List;

public interface Seeker  {
    List<SeekContext> seek(SeekContext context);
}