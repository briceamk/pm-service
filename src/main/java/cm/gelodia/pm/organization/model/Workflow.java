package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_workflows")
public class Workflow extends UserDateAudit {
    @Column(nullable = false, length = 32)
    private String code;
    @Column(nullable = false, length = 64)
    private String name;
    private String description;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private WorkflowType type;
    @OneToMany(mappedBy = "workflow", orphanRemoval = true)
    private List<Level> levels;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Workflow(String id, String code, String name, String description, Boolean active,
                    List<Level> levels, Company company, WorkflowType type) {
        super(id);
        this.code = code;
        this.name = name;
        this.description = description;
        this.type = type;
        this.active = active;
        this.levels = levels;
        this.company = company;
    }
}
