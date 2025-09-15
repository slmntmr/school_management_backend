package com.example.school_management_backend.service;

import com.example.school_management_backend.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Kullanıcının rolünü alıp GrantedAuthority listesine dönüştür
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Şifreyi döndür
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Kullanıcı adı olarak e-posta
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Hesap süresi dolmuş mu?
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Hesap kilitli mi?
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Kimlik bilgileri süresi dolmuş mu?
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // Kullanıcı aktif mi?
    }

    // Ekstra kullanıcı bilgilerine erişim için getter metodları
    public Long getId() {
        return user.getId();
    }

    public String getFullName() {
        return user.getFullName();
    }

    public String getClassroom() {
        return user.getClassroom();
    }

    public String getSchoolNumber() {
        return user.getSchoolNumber();
    }
}
