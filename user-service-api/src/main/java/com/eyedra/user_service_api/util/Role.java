package com.eyedra.user_service_api.util;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    LISTENER,
    ADMINISTRATOR,;

    @Override
    public String getAuthority() {
        return name();
    }
}
