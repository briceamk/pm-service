package cm.gelodia.pm.catalog.model;


import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "catalog_products")
public class Product extends UserDateAudit {

    @Column(nullable = false, length = 32)
    private String reference;
    @Column(nullable = false, length = 64)
    private String name;
    private String description;
    private BigDecimal standardCostPrice;
    private String imageType;
    private String imageName;
    private Boolean active;
    // TODO we will use this field with attachment service
    //private String attachmentId;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Product(String id, String reference, String name, String description, BigDecimal standardCostPrice,
                   String imageType, String imageName, Boolean active, Category category, Catalog catalog) {

        super(id);
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.standardCostPrice = standardCostPrice;
        this.imageType = imageType;
        this.imageName = imageName;
        this.active = active;
        this.category = category;
        this.catalog = catalog;
    }
}
