package com.gazellig.amqpservice.amqp.audit.replay;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gazellig.amqpservice.amqp.util.LocalDateTimeToTick;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * AuditReplayService handles building and sending ReplayCommands to the AuditLog.
 * <p>
 * The request has to be send after the Queue is initialized to ensure no events are lost.
 * <p>
 * A counter is kept for how many of the AuditLog requests are handled. Once all requests are handled
 * all other listeners are started and the AuditlogReplay queue is closed.
 */
@Log4j2
@Service
public class AuditReplayService {
    private RestTemplate restTemplate;
    private String auditlogUrl;
    private String exchangeName;
    private ObjectMapper objectMapper;
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    private AtomicInteger amountToReceive;
    private Multimap<Integer, HttpEntity<String>> topicsToRequest;

    @Autowired
    public AuditReplayService(@Value("${auditlog.url}") String url,
                              @Value("${auditlog.port}") String port,
                              @Value("${auditlog.replay.exchange}") String exchange,
                              RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry,
                              ObjectMapper objectMapper,
                              RestTemplate restTemplate) {
        this.auditlogUrl = url + ":" + port + "/ReplayEvents";
        this.exchangeName = exchange;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.amountToReceive = new AtomicInteger();
        this.topicsToRequest = MultimapBuilder.treeKeys().linkedListValues().build();
        this.rabbitListenerEndpointRegistry = rabbitListenerEndpointRegistry;
    }

    /**
     * Add a new topic to be requested by the AuditLog. This is generally called automatically
     * when registering a new receiver at the {@link AuditReplayEventReceiver}
     *
     * @param topic Topic to register for a request
     * @return entity created with set topic
     */
    HttpEntity<String> addNewReplayRequest(String topic, int priority) {
        var command = buildEventsCommand(topic);
        var entity = buildRequestEntity(command);
        topicsToRequest.put(priority, entity);
        return entity;
    }

    /**
     * Mark an AuditLog event as received, decreasing the total amount of events to receive by one.
     * <p>
     * When the counter hits 0 the {@link AuditReplayEventReceiver} is closed
     * and all other registered listeners are started.
     *
     * @return toReceive amount left after decrementing
     */
    public int decreaseToReceive() {
        int toReceive = amountToReceive.decrementAndGet();

        if (toReceive == 0) {
            startReceivers();
        }

        return toReceive;
    }

    /**
     * Send all currently registered requests to the AuditLog.
     * <p>
     * This function is called automatically on startup and should generally not be called manually.
     * <p>
     * Received responses contain the amount of expected events to be send and get added to amountToReceive.
     */
    void sendAllRegisteredRequests() {
        AtomicInteger counter = new AtomicInteger(topicsToRequest.size());
        topicsToRequest.forEach((priority, stringHttpEntity) -> {
            counter.decrementAndGet();
            sendEventsRequestEntity(stringHttpEntity, counter.get());
        });
    }

    // package private for testing
    int addToAmountToReceive(Integer amount, int count) {
        int toReceive = amountToReceive.addAndGet(amount);
        if (toReceive == 0 && count == 0) {
            startReceivers();
        }

        return toReceive;
    }


    private void sendEventsRequestEntity(HttpEntity<String> entity, int count) {
        log.info(String.format("Sending EventsRequest for topic %s", entity));
        ResponseEntity<Integer> result = restTemplate.exchange(auditlogUrl, HttpMethod.POST, entity, Integer.class);
        addToAmountToReceive(result.getBody(), count);
        log.info(String.format("Receiving %s results", result.getBody()));
    }

    private void startReceivers() {
        Set<String> listenerContainerIds = rabbitListenerEndpointRegistry.getListenerContainerIds();

        listenerContainerIds.forEach(id -> {
            if (!id.equals("AuditReplayQueue")) {
                rabbitListenerEndpointRegistry.getListenerContainer(id).start();
                log.info(String.format("Starting listener %s", id));

            }

            if (id.equals("AuditReplayQueue")) {
                rabbitListenerEndpointRegistry.getListenerContainer(id).stop();
                log.info(String.format("Stopping listener %s", id));
            }
        });
    }

    private HttpEntity<String> buildRequestEntity(AuditReplayCommand auditReplayCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "";

        try {
            json = objectMapper.writeValueAsString(auditReplayCommand);
        } catch (JsonProcessingException e) {
            log.error("Error during Command building", e);
        }

        return new HttpEntity<>(json, headers);
    }

    private AuditReplayCommand buildEventsCommand(String topicFilter) {
        return AuditReplayCommand.builder()
                .exchangeName(exchangeName)
                .toTimestamp(LocalDateTimeToTick.toTick(LocalDateTime.now()))
                .eventType(null)
                .topicFilter(topicFilter)
                .build();
    }
}
