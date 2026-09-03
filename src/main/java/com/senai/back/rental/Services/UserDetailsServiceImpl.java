package com.senai.back.rental.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Injete seu UsuarioRepository aqui
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Substitua pela consulta ao seu repositório de usuários
        return null; 
    }
}