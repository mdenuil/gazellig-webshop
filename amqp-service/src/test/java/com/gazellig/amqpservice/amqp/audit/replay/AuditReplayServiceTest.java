package com.gazellig.amqpservice.amqp.audit.replay;

import java.util.Optional;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditReplayServiceTest {
    @Mock
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
    @Mock
    private RestTemplate restTemplate;
    private String url = "http://url.com";
    private String port = "2022";
    private String exchange = "Kantilever.Replay";
    private String topicFilter = "Kantilever.*.*";
    private ObjectMapper objectMapper = new ObjectMapper();
    private AuditReplayService auditReplayService;


    @BeforeEach
    void init() {
        auditReplayService = new AuditReplayService(
                url,
                port,
                exchange,
                rabbitListenerEndpointRegistry,
                objectMapper,
                restTemplate
        );
    }

    @Test
    @DisplayName("When adding a new TopicRequest the correct Entity is build and added to the list")
    void addNewTopicToRequest_buildsCorrectEntity_andAddsToList() {
        // Arrange
        String topic = "new.topic";
        // Act
        var entity = auditReplayService.addNewReplayRequest(topic, 100);
        // Assert
        assertThat(entity.getBody().contains(topic)).isTrue();
        assertThat(entity.getBody().contains(exchange)).isTrue();
    }

    @Test
    @DisplayName("The toReceive counter is correctly decremented when decreaseToReceive is called")
    void decreaseToReceive_correctlyDecrements_toReceiveValue() {
        // Arrange
        auditReplayService.addToAmountToReceive(10, 0);
        // Act
        int actual = auditReplayService.decreaseToReceive();
        // Assert
        assertThat(actual).isEqualTo(9);
    }

    @Test
    @DisplayName("When toReceive counter hits 0 queues are correctly opened")
    void decreaseToReceive_correctlyOpensQueues() {
        // Arrange
        auditReplayService.addToAmountToReceive(1, 2);
        Set<String> mockedIds = Set.of("OtherQueue", "AnotherQueue");
        var mockedOtherListener = mock(MessageListenerContainer.class);
        var mockedAnotherListener = mock(MessageListenerContainer.class);

        when(rabbitListenerEndpointRegistry.getListenerContainerIds()).thenReturn(mockedIds);
        when(rabbitListenerEndpointRegistry.getListenerContainer("OtherQueue")).thenReturn(mockedOtherListener);
        when(rabbitListenerEndpointRegistry.getListenerContainer("AnotherQueue")).thenReturn(mockedAnotherListener);
        // Act
        int actual = auditReplayService.decreaseToReceive();
        // Assert
        assertThat(actual).isEqualTo(0);
        verify(mockedOtherListener, times(1)).start();
        verify(mockedAnotherListener, times(1)).start();
    }

    @Test
    @DisplayName("When addToAmountToReceive counter hits 0 queues are correctly kept closed if more requests are left")
    void addToAmountToReceive_correctlyKeepsQueuesClosed_whenMoreRequestsAreLeft() {
        // Arrange
        var mockedOtherListener = mock(MessageListenerContainer.class);
        var mockedAnotherListener = mock(MessageListenerContainer.class);
        // Act
        int actual = auditReplayService.addToAmountToReceive(1, 2);
        // Assert
        assertThat(actual).isEqualTo(1);
        verify(mockedOtherListener, times(0)).start();
        verify(mockedAnotherListener, times(0)).start();
    }

    @Test
    @DisplayName("When toReceive counter hits 0 queues are correctly opened if no more requests are left")
    void addToAmountToReceive_correctlyOpensQueues_whenNoMoreRequestsAreLeft() {
        // Arrange
        Set<String> mockedIds = Set.of("OtherQueue", "AnotherQueue");
        var mockedOtherListener = mock(MessageListenerContainer.class);
        var mockedAnotherListener = mock(MessageListenerContainer.class);

        when(rabbitListenerEndpointRegistry.getListenerContainerIds()).thenReturn(mockedIds);
        when(rabbitListenerEndpointRegistry.getListenerContainer("OtherQueue")).thenReturn(mockedOtherListener);
        when(rabbitListenerEndpointRegistry.getListenerContainer("AnotherQueue")).thenReturn(mockedAnotherListener);
        // Act
        int actual = auditReplayService.addToAmountToReceive(0, 0);
        // Assert
        assertThat(actual).isEqualTo(0);
        verify(mockedOtherListener, times(1)).start();
        verify(mockedAnotherListener, times(1)).start();
    }


    @Test
    @DisplayName("SendAllRegistered requests correctly sends a request currently registered")
    void sendAllRegisteredRequests_correctlySendsRequest_forEachSavedRequest() {
        // Arrange
        auditReplayService.addNewReplayRequest(topicFilter, 100);
        when(restTemplate
                .exchange(anyString(), eq(HttpMethod.POST), any(), eq(Integer.class)))
                .thenReturn(ResponseEntity.of(Optional.of(500)));
        // Act
        auditReplayService.sendAllRegisteredRequests();
        // Assert
        verify(restTemplate, times(1)).exchange(
                eq(url + ":" + port + "/ReplayEvents"),
                eq(HttpMethod.POST),
                any(),
                eq(Integer.class)
        );
    }

    @Test
    @DisplayName("SendAllRegistered requests correctly add result to the toReceive")
    void sendAllRegisteredRequests_correctlyAddsResult_toToReceive() {
        // Arrange
        auditReplayService.addNewReplayRequest(topicFilter, 100);
        when(restTemplate
                .exchange(anyString(), eq(HttpMethod.POST), any(), eq(Integer.class)))
                .thenReturn(ResponseEntity.of(Optional.of(500)));
        // Act
        auditReplayService.sendAllRegisteredRequests();
        // Assert
        int actual = auditReplayService.addToAmountToReceive(0, 0);
        assertThat(actual).isEqualTo(500);
        verify(restTemplate, times(1)).exchange(
                eq(url + ":" + port + "/ReplayEvents"),
                eq(HttpMethod.POST),
                any(),
                eq(Integer.class)
        );
    }

    @Test
    @DisplayName("SendAllRegistered requests with Requests with different priorities get requested in the right order")
    void sendAllRegistered_withDifferentPriorities_getRequestInTheCorrectOrder() {
        // Arrange
        auditReplayService.addNewReplayRequest(topicFilter, 100);
        auditReplayService.addNewReplayRequest(topicFilter, 90);
        auditReplayService.addNewReplayRequest(topicFilter, 110);
        auditReplayService.addNewReplayRequest(topicFilter, 80);
        auditReplayService.addNewReplayRequest(topicFilter, 100);
        when(restTemplate
                .exchange(anyString(), eq(HttpMethod.POST), any(), eq(Integer.class)))
                .thenReturn(ResponseEntity.of(Optional.of(500)));
        // Act
        auditReplayService.sendAllRegisteredRequests();
        // Assert
        int actual = auditReplayService.addToAmountToReceive(0, 0);
        assertThat(actual).isEqualTo(2500);
        verify(restTemplate, times(5)).exchange(
                eq(url + ":" + port + "/ReplayEvents"),
                eq(HttpMethod.POST),
                any(),
                eq(Integer.class)
        );
    }


    @Test
    @DisplayName("SendAllRegistered requests correctly sends multiple requests currently registered")
    void sendAllRegisteredRequests_correctlySendsMultipleRequests_forEachSavedRequest() {
        auditReplayService.addNewReplayRequest(topicFilter, 100);
        auditReplayService.addNewReplayRequest(topicFilter + "1", 100);
        // Arrange
        when(restTemplate
                .exchange(anyString(), eq(HttpMethod.POST), any(), eq(Integer.class)))
                .thenReturn(ResponseEntity.of(Optional.of(500)));
        // Act
        auditReplayService.sendAllRegisteredRequests();
        // Assert
        verify(restTemplate, times(2)).exchange(
                eq(url + ":" + port + "/ReplayEvents"),
                eq(HttpMethod.POST),
                any(),
                eq(Integer.class)
        );
    }

    @Test
    @DisplayName("With one Queue and that queue returns 0 results, the AuditReplay queue is closed")
    void sendAllRegisteredRequests_correctlyClosesAuditQueue_whenOneReceiverIsRegister_AndZeroResultsAreReturned() {
        auditReplayService.addNewReplayRequest(topicFilter, 100);
        Set<String> mockedIds = Set.of("AuditReplayQueue");
        var mockedReplayListener = mock(MessageListenerContainer.class);

        when(rabbitListenerEndpointRegistry.getListenerContainerIds()).thenReturn(mockedIds);
        when(rabbitListenerEndpointRegistry.getListenerContainer("AuditReplayQueue")).thenReturn(mockedReplayListener);
        // Arrange
        when(restTemplate
                .exchange(anyString(), eq(HttpMethod.POST), any(), eq(Integer.class)))
                .thenReturn(ResponseEntity.of(Optional.of(0)));
        // Act
        auditReplayService.sendAllRegisteredRequests();
        // Assert
        verify(restTemplate, times(1)).exchange(
                eq(url + ":" + port + "/ReplayEvents"),
                eq(HttpMethod.POST),
                any(),
                eq(Integer.class)
        );
        verify(mockedReplayListener, times(1)).stop();

    }

    @Test
    @DisplayName("When toReceive counter hits 0 the ReplayQueue is correctly closed")
    void decreaseToReceive_correctlyCloses_replayQueue() {
        // Arrange
        auditReplayService.addToAmountToReceive(1, 0);
        Set<String> mockedIds = Set.of("AuditReplayQueue");
        var mockedReplayListener = mock(MessageListenerContainer.class);

        when(rabbitListenerEndpointRegistry.getListenerContainerIds()).thenReturn(mockedIds);
        when(rabbitListenerEndpointRegistry.getListenerContainer("AuditReplayQueue")).thenReturn(mockedReplayListener);

        // Act
        int actual = auditReplayService.decreaseToReceive();
        // Assert
        assertThat(actual).isEqualTo(0);
        verify(mockedReplayListener, times(1)).stop();
    }
}