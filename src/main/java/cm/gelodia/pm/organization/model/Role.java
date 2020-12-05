package cm.gelodia.pm.organization.model;


import cm.gelodia.pm.auth.model.Permission;
import cm.gelodia.pm.commons.autdit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_roles")
public class Role extends DateAudit {

    @Column(length = 60, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private RoleType type;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "organisation_roles_permissions_rel",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
    )
    private Set<Permission> permissions;

    @Builder
    public Role(String id, String name, RoleType type, Set<Permission> permissions) {
        super(id);
        this.name = name;
        this.type = type;
        this.permissions = permissions;
    }
}
