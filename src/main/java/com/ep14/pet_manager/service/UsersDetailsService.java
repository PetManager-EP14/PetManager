package com.ep14.pet_manager.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import com.ep14.pet_manager.repository.UserRepository;

@Service
public class UsersDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    
    public UsersDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        com.ep14.pet_manager.entity.User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario No Encontrado!"));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().getCode().toUpperCase()));
        return new User(u.getEmail(), u.getPassword_hash(), authorities);

    }
}
