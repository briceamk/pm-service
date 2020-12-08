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
@Table(name = "organization_partners")
public class Partner extends UserDateAudit {
    @Column(nullable = false, length = 8)
    private String code;
    @Column(nullable = false, length = 64)
    private String name;
    private String description;
    private String vat;
    private String trn;
    @Column(length = 64)
    private String street;
    @Column(length = 64)
    private String street1;
    @Column(length = 16)
    private String zip;
    @Column(length = 128)
    private String email;
    @Column(length = 16)
    private String phone;
    @Column(length = 16)
    private String mobile;
    @Column(length = 16)
    private String fax;
    @Column(length = 128)
    private String website;
    @Column(length = 64)
    private String city;
    @Column(length = 64)
    private String country;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Partner(String id, String code, String name, String description, String vat, String trn,
                   String street, String street1, String zip, String email, String phone, String mobile,
                   String fax, String website, String city, String country, Company company) {
        super(id);
        this.code = code;
        this.name = name;
        this.description = description;
        this.vat = vat;
        this.trn = trn;
        this.street = street;
        this.street1 = street1;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.fax = fax;
        this.website = website;
        this.city = city;
        this.country = country;
        this.company = company;
    }
}
