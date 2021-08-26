package mk.ukim.finki.photography.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_PHOTOGRAPHER;

    @Override
    public String getAuthority() {
        return name();
    }
}
