package cm.gelodia.pm.auth.model;

import cm.gelodia.pm.commons.autdit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_permissions")
public class Permission extends DateAudit {
    @Column(nullable = false, unique = true, length = 64)
    @Enumerated(EnumType.STRING)
    private PermissionCode code;
    @Column(nullable = false, unique = true, length = 64)
    private String name;

    @Builder
    public Permission(String id, PermissionCode code, String name) {
        super(id);
        this.code = code;
        this.name = name;
    }
}
