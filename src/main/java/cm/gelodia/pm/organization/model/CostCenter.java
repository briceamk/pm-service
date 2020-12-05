package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.auth.model.User;
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
@Table(name = "organization_cost_centers")
public class CostCenter extends UserDateAudit {
    @Column(nullable = false, length = 32)
    private String code;
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 64)
    private String externalId;
    @OneToMany(mappedBy = "costCenter", fetch = FetchType.LAZY)
    private List<WorkflowInstance> workflowInstances;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public CostCenter(String id, String code, String name, String externalId,
                      List<WorkflowInstance> workflowInstances, Company company) {
        super(id);
        this.code = code;
        this.name = name;
        this.externalId = externalId;
        this.workflowInstances = workflowInstances;
        this.company = company;
    }
}
