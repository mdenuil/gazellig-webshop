package com.kantilever.bffwebwinkel.security.jwt;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthenticationException authException;


    private AuthEntryPointJwt authEntryPointJwt;

    @BeforeEach
    void init() {
        this.authEntryPointJwt = new AuthEntryPointJwt();
    }

    @Test
    @DisplayName("commence sets Unauthorized header on response")
    void commenceSetsUnauthorizedHeader() throws IOException {
        // Arrange
        // Act
        authEntryPointJwt.commence(request, response, authException);
        // Assert
        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }

    @Test
    @DisplayName("commence propagates IOException to Spring Security")
    void commencePropagatesExceptions() throws IOException {
        // Arrange
        doThrow(new IOException()).when(response).sendError(anyInt(), any());
        // Act
        // Assert
        assertThatThrownBy(() -> authEntryPointJwt.commence(request, response, authException)).isInstanceOf(IOException.class);
    }
}
