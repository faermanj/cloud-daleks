package scar.seek;

import java.util.List;

public interface Seeker  {
    String ANY = "*";

    List<SeekContext> seek(SeekContext context);
}