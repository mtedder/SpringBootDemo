package com.gc.dto;

/**
 * Created by maurice on 8/20/17.
 * Ref: http://www.baeldung.com/spring-security-authentication-with-a-database
 */
public class CustomUserDetails {
    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    
    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
