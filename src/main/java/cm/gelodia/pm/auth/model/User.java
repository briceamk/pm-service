package cm.gelodia.pm.auth.model;


import cm.gelodia.pm.commons.autdit.DateAudit;
import cm.gelodia.pm.company.model.Company;
import cm.gelodia.pm.organization.model.Department;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_users")
public class User extends DateAudit {


    @Column(length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(nullable = false, length = 256)
    private String email;

    @Column(unique = true, length = 16)
    private String mobile;

    private String city;

    @Column(nullable = false, length = 512)
    private String password;

    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auth_users_permissions_rel",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    @Builder
    public User(String id, String firstName, String lastName, String username, String email,
                String mobile, String city, String password, Boolean accountNonExpired,
                Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled,
                Company company, Department department, Set<Permission> permissions) {
        super(id);
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
        this.department = department;
        this.permissions = permissions;
    }
}
