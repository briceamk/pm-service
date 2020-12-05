package cm.gelodia.pm.organization.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_charges")
public class Charge extends UserDateAudit {
    @Column(nullable = false, length = 32)
    private String code;
    @Column(nullable = false, length = 64)
    private String name;
    private Boolean isInternational;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Charge(String id, String code, String name, Boolean isInternational, Boolean active,
                  Company company) {
        super(id);
        this.code = code;
        this.name = name;
        this.isInternational = isInternational;
        this.active = active;
        this.company = company;
    }
}
