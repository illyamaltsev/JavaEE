package com.kma.practice8.springsecuritydb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kma.practice8.springsecuritydb.domain.entities.PermissionEntity;
import com.kma.practice8.springsecuritydb.domain.entities.UserEntity;
import com.kma.practice8.springsecuritydb.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final UserEntity user = userRepository.findByLogin(username)
            .orElseThrow(() -> new UsernameNotFoundException("No user with login: " + username));

        return User.builder()
            .username(username)
            .password(user.getPassword())
            .authorities(mapAuthorities(user.getPermissions()))
            .build();
    }

    private static List<GrantedAuthority> mapAuthorities(final List<PermissionEntity> permissions) {
        return permissions.stream()
            .map(PermissionEntity::getPermission)
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableList());
    }
}
