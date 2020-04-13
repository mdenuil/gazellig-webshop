package com.gazellig.amqpservice.amqp.audit.replay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuditReplayCommand represents the structure for POST requests to the AuditLog. These requests
 * specify a topic and the AuditLog replays all events for that topic.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class AuditReplayCommand {
    private String exchangeName;
    private long toTimestamp;
    private String eventType;
    private String topicFilter;
}
