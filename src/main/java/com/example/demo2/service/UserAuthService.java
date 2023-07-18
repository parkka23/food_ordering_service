package com.example.demo2.service;

import com.example.demo2.entity.Client;
import com.example.demo2.repository.ClientRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    private ClientRepositoryImpl clientRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> optionalClient = Optional.ofNullable(clientRepository.findByEmail(email));
        if (!optionalClient.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }
        Client account = optionalClient.get();
        List<GrantedAuthority> grantedAuthorities = account
                .getRoles()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), grantedAuthorities); // (2)
    }
}