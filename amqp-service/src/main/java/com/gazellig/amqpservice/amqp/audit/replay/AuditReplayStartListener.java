package com.gazellig.amqpservice.amqp.audit.replay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener for when to send the AuditReplayCommand. This has to be done after the application is fully started.
 * <p>
 * This bean exists primarily for test as it can be Mocked and the default request won't be send.
 */
@Component
public class AuditReplayStartListener {

    private AuditReplayService auditReplayService;

    @Autowired
    public AuditReplayStartListener(AuditReplayService auditReplayService) {
        this.auditReplayService = auditReplayService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startAuditLogReplay() {
        auditReplayService.sendAllRegisteredRequests();
    }
}
