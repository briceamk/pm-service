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
@Table(name = "catalog_catalogs")
public class Catalog extends UserDateAudit {

    @Column(nullable = false, length = 64)
    private String name;
    private String description;
    private Boolean active;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Catalog parent;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Catalog(String id, String name, String description, Boolean active, Catalog parent,
                   Company company) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = active;
        this.parent = parent;
        this.company = company;
    }
}
