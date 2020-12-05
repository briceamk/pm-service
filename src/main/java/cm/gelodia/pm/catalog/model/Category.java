package cm.gelodia.pm.catalog.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "catalog_categories")
public class Category extends UserDateAudit {

    @Column(nullable = false, length = 64)
    private String  name;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "parent_id", updatable = false)
    private Category parent;

    @Builder
    public  Category(String id, String name, Boolean active, Category parent, Company company) {
        super(id);
        this.name = name;
        this.active = active;
        this.parent = parent;
        this.company = company;
    }
}
