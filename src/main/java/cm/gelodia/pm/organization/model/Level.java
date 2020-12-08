package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_levels")
public class Level extends UserDateAudit {
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;
    @OneToMany(mappedBy = "level", orphanRemoval = true)
    private List<Step>  steps;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
