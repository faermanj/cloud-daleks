package scar.report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ReportUtils {
    // format localdatetime to iso
    public static String format(LocalDateTime dateTime) {
        var result = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME  );
        return result;
    }   
}
