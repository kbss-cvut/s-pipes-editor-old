package cz.cvut.kbss.sempipes.security.model;

import cz.cvut.kbss.sempipes.model.view.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private static final Map<String, String> ROLE_MAPPING = initRoleMapping();

    private Person user;

    private final Set<GrantedAuthority> authorities;

    private static Map<String, String> initRoleMapping() {
        final Map<String, String> result = new HashMap<>();
        result.put("https://admin", "ROLE_ADMIN");
        return result;
    }

    public UserDetails(Person user) {
        Objects.requireNonNull(user);
        this.user = user;
        this.authorities = new HashSet<>();
        resolveRoles();
    }

    public UserDetails(Person user, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(authorities);
        this.user = user;
        this.authorities = new HashSet<>();
        resolveRoles();
        this.authorities.addAll(authorities);
    }

    private void resolveRoles() {
        authorities.addAll(ROLE_MAPPING.entrySet().stream().filter(e -> user.getTypes().contains(e.getKey()))
                .map(e -> new SimpleGrantedAuthority(e.getValue()))
                .collect(Collectors.toList()));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /*public void eraseCredentials() {
        user.erasePassword();
    }*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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

    public Person getUser() {
        return user;
    }
}
