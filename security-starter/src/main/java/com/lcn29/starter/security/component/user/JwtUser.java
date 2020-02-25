package com.lcn29.starter.security.component.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * <pre>
 * Jwt User
 * </pre>
 *
 * @author LCN
 * @date 2020-02-25 16:00
 */
public class JwtUser implements UserDetails{

    private final String username;

    private final String password;

    /** is the account available */
    private final boolean enabled;

    private final Date lastPasswordResetDate;

    /** all roles of user */
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(String username, String password, boolean enabled, Date lastPasswordResetDate,
                   Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
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

    /**
     * the account have expired ?
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * the account is locked ?
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * the credentials of account is expired
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

}
