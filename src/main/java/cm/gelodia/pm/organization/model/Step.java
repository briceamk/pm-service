package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_workflows")
public class Step  extends UserDateAudit {

    @Column(nullable = false, length = 4)
    private String sequence;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private StepType type;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @ManyToOne
    @JoinColumn(name = "level_id")
    private Level level;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Step(String id, String sequence, StepType type, User user, Role role,
                Level level, Company company) {
        super(id);
        this.sequence = sequence;
        this.type = type;
        this.user = user;
        this.role = role;
        this.level = level;
        this.company = company;
    }
}
