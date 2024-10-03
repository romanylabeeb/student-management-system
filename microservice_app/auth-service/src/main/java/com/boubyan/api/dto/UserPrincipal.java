package com.boubyan.api.dto;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.boubyan.api.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public UserPrincipal(User user) {
        this.id=user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }
    public Long getId() {
        return id;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {

        return password;
    }

    @Override
    public String getUsername() {

        return username;
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

}