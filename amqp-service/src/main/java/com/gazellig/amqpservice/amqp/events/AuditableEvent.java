package com.gazellig.amqpservice.amqp.events;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gazellig.amqpservice.amqp.util.LocalDateTimeToTickSerializer;
import com.gazellig.amqpservice.amqp.util.TickToLocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuditableEvent represents the base of every payload and contains the fields relevant
 * to a specific even.
 * <p>
 * All Events must extend this Base in order to be valid.
 * <p>
 * JSON Properties are mapped as known to supertypes of this base event. If you're adding subtypes
 * of this class make sure to include it as a subtype in the @JsonSubtype annotation.
 * The name arg is the topic of that specific event subtype.
 * <p>
 * See <a href="https://www.baeldung.com/jackson-inheritance"></a> for details on Jackson Inheritance.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true,
        property = "Topic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditableEvent {
    @JsonProperty("Timestamp")
    @JsonSerialize(using = LocalDateTimeToTickSerializer.class)
    @JsonDeserialize(using = TickToLocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    @JsonProperty("CorrelationId")
    private UUID correlationId;
    @JsonProperty("Topic")
    private String topic;

    public AuditableEvent(String topic) {
        this.topic = topic;
        this.correlationId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
    }
}
