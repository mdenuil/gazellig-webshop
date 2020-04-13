package com.kantilever.bffwebwinkel.security.services;

import com.kantilever.bffwebwinkel.domain.klant.models.Klant;
import com.kantilever.bffwebwinkel.domain.klant.repositories.KlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsServiceImpl provides a method of verifying that a User exists.
 * <p>
 * Users are compared by a unique email.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private KlantRepository klantRepository;

    @Autowired
    public UserDetailsServiceImpl(KlantRepository klantRepository) {
        this.klantRepository = klantRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        Klant klant = klantRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return UserDetailsImpl.build(klant);
    }
}
