package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_departments")
public class WorkflowInstance extends UserDateAudit {

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private WorkflowInstanceType type;
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private WorkflowInstanceObject object;
    @ManyToOne
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;
    @ManyToOne
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Builder
    public WorkflowInstance(String id, WorkflowInstanceType type, WorkflowInstanceObject object,
                            Workflow workflow, CostCenter costCenter, Department department) {
        super(id);
        this.type = type;
        this.object = object;
        this.workflow = workflow;
        this.costCenter = costCenter;
        this.department = department;
    }
}
