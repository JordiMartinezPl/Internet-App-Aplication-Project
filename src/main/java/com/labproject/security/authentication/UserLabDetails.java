package com.labproject.security.authentication;

import com.labproject.domain.CollectionUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserLabDetails implements UserDetails {
    private final CollectionUser userLab;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserLabDetails(CollectionUser userLab) {
        this.userLab = userLab;
        if (userLab.getRole() != null) {
            this.authorities = List.of(new SimpleGrantedAuthority(userLab.getRole().getName().name()));
        } else {
            this.authorities = List.of();
        }
    }


    @Override
    public String getPassword() {
        return userLab.getPassword();
    }

    @Override
    public String getUsername() {
        return userLab.getEmail();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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

    public long getId() {
        return userLab.getId();
    }

    @Override
    public String toString() {
        return "UserLab{" +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", roles=" + authorities.toString() +
                '}';
    }

}
