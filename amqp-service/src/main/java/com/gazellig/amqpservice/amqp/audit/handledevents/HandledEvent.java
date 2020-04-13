package com.gazellig.amqpservice.amqp.audit.handledevents;

import javax.persistence.Entity;
import javax.persistence.Id;
import com.gazellig.amqpservice.amqp.events.AuditableEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents an {@link AuditableEvent} from the Auditlog that is already handled.
 * <p>
 * Stores the correlationId of the payload.
 */
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
class HandledEvent {
    @Id
    private String correlationId;
}
