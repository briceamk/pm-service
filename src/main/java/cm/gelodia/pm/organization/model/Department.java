package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_departments")
public class Department extends UserDateAudit {

    @Column(nullable = false, length = 32)
    private String code;
    @Column(nullable = false, length = 64)
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Department parent;
    @ManyToOne
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter; // this is the default cost center
    @ManyToOne
    @JoinColumn(name = "address_id") // this is the default address
    private Address address;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "organization_departments_users_rel",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Address> addresses;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<WorkflowInstance> workflowInstances;


    @Builder
    public Department(String id, String code, String name, Department parent, CostCenter costCenter,
                      Address address, List<User> users, List<Address> addresses, Company company,
                      List<WorkflowInstance> workflowInstances) {
        super(id);
        this.code = code;
        this.name = name;
        this.parent = parent;
        this.costCenter = costCenter;
        this.address = address;
        this.users = users;
        this.addresses = addresses;
        this.company = company;
        this.workflowInstances = workflowInstances;
    }
}
