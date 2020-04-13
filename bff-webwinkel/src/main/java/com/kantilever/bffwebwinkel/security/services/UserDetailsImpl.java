package com.kantilever.bffwebwinkel.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetailsImpl holds the relevant Authentication and Authorization information for a {@link Klant}.
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String email;
    @JsonIgnore
    private String wachtwoord;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String wachtwoord,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.wachtwoord = wachtwoord;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Klant klant) {
        List<GrantedAuthority> authorities = klant.getKlantSoort().stream()
                .map(role -> new SimpleGrantedAuthority(role.getNaam().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                klant.getKlantNummer(),
                klant.getEmail(),
                klant.getWachtwoord(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return wachtwoord;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, wachtwoord, authorities);
    }
}
