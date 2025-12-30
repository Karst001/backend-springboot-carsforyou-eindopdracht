package nl.carsforyou.garage.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

public class DateValidationUtil {
//    private DateValidationUtil() {}

    //helper to check if the selecfted date is prior to todays date for example or a completed date is prior to start date
    public static void validateDateOrder(
            LocalDateTime start,
            LocalDateTime end,
            String startDateName,
            String endDateName
    ) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    endDateName + " cannot be before " + startDateName
            );
        }
    }
}
