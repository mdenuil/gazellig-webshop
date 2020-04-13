package com.gazellig.amqpservice.amqp.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * JSON Serializer to automatically convert LocalDateTimes to C# ticks.
 */
public class LocalDateTimeToTickSerializer extends LocalDateTimeSerializer { // NOSONAR

    public LocalDateTimeToTickSerializer() {
        super(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }


    @Override
    public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider) throws IOException {
        if (useTimestamp(provider)) {
            g.writeNumber(LocalDateTimeToTick.toTick(value));
        }

        super.serialize(value, g, provider);
    }
}
