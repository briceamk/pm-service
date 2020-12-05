package cm.gelodia.pm.auth.security;

import cm.gelodia.pm.auth.model.Authority;
import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.company.model.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Setter
@ToString
public class UserPrincipal implements UserDetails {
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String mobile;

    private String city;

    @JsonIgnore
    private String password;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    private Company company;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String id, String firstName, String lastName, String username, String email, String mobile, String city,
                         String password, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled,
                         Company company, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.city = city;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.company = company;
        this.authorities = authorities;
    }

    public static UserPrincipal create(User user) {
        Set<GrantedAuthority> authorities = user.getPermissions().stream().map(permission
                -> new Authority(permission.getCode().name())).collect(Collectors.toSet());

        return new UserPrincipal(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getMobile(),
                user.getCity(),
                user.getPassword(),
                user.getAccountNonExpired(),
                user.getAccountNonLocked(),
                user.getCredentialsNonExpired(),
                user.getEnabled(),
                user.getCompany(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Company getCompany() {
        return company;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCity() {
        return city;
    }
}
