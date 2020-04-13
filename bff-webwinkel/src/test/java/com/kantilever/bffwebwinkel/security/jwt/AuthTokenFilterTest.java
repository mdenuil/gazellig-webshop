package com.kantilever.bffwebwinkel.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kantilever.bffwebwinkel.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private UserDetails userDetails;
    @Mock
    private SecurityContext securityContext;


    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void init() {
        authTokenFilter = new AuthTokenFilter(jwtUtils, userDetailsService);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Invalid Authorization header gets ignored and passed on into the filter chain")
    void invalidAuth_getsHandledSilently() throws IOException, ServletException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("");
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Assert
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    @DisplayName("Valid Authorizaiton header but invalid JWT token get ignored and passed on in the filter chain")
    void validAuth_invalidJWT_getsHandledSilently() throws IOException, ServletException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer JWT");
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(false);
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Assert
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(any(), any());
    }

    @Test
    @DisplayName("When JWT is valid and user is found, authentication is set in SecurityContextHolder")
    void validAuth_setsAuthInContext() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer JWT");
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("JWT")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Assert
        verify(securityContext, times(1)).setAuthentication(any());
    }

    @Test
    @DisplayName("When JWT is valid but user not found filter throws UserNotFoundException")
    void userNotFound_throwsUserNotFoundException() {
        when(request.getHeader("Authorization")).thenReturn("Bearer JWT");
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("JWT")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenThrow(UsernameNotFoundException.class);
        // Act
        // Assert
        assertThatThrownBy(() -> authTokenFilter.doFilterInternal(request, response, filterChain)).isInstanceOf(UsernameNotFoundException.class);
        verify(securityContext, times(0)).setAuthentication(any());
    }
}