package com.rootanto.airline.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    private String email;
    private String password;
    private List<UserRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        //TODO: Implementar
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //TODO: Implementar
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //TODO: Implementar
        return true;
    }

    @Override
    public boolean isEnabled() {
        //TODO: Implementar
        return true;
    }
}
