package com.gazellig.amqpservice.amqp.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

/**
 * JSON Deserializer to automatically convert C# ticks to LocalDateTimes
 */
public class TickToLocalDateTimeDeserializer extends LocalDateTimeDeserializer { // NOSONAR

    public TickToLocalDateTimeDeserializer() {
        super(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            long value = parser.getValueAsLong();
            return LocalDateTimeToTick.toLocalDateTime(value);
        }

        return super.deserialize(parser, context);
    }

}
