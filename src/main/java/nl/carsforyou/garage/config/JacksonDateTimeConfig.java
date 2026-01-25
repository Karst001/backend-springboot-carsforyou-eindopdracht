package nl.carsforyou.garage.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class JacksonDateTimeConfig {

    @Bean
    public Module localDateTimeLenientModule() {
        SimpleModule module = new SimpleModule();

        //custom Jackson deserializer for LocalDateTime to allow "appointmentDate": "2026-01-26T16:16:24.262", or "appointmentDate": "......Z",
        //In swagger for example the same Schema uses a 'Z' but user can delete that as well, make it fail proof
        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
                String raw = p.getText();
                if (raw == null || raw.isBlank()) return null;

                //1) Try a plain LocalDateTime like 2026-01-25T15:55:28.716
                try {
                    return LocalDateTime.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException ignored) {}

                //2) Try a Offset/Zoned/Instant formats like . ...Z or ...+01:00 and drop the offset
                try {
                    return OffsetDateTime.parse(raw, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();
                } catch (DateTimeParseException ignored) {}

                try {
                    return Instant.parse(raw).atZone(ZoneOffset.UTC).toLocalDateTime();
                } catch (DateTimeParseException ignored) {}

                //Nothing matched, return a proper 400
                throw new IOException("Invalid date-time value for LocalDateTime: " + raw);
            }
        });

        return module;
    }
}
