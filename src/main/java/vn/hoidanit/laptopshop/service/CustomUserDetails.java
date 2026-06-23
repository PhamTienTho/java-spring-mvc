package vn.hoidanit.laptopshop.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User {

    private long id;
    private String fullName;
    private String avatar;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
            long id, String fullName, String avatar) {

        super(username, password, authorities);
        this.id = id;
        this.fullName = fullName;
        this.avatar = avatar;
        
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }
}
