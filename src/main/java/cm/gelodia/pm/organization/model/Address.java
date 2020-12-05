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
@Table(name = "organization_addresses")
public class Address extends UserDateAudit {
    @Column(nullable = false, length = 64)
    private String name;
    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private AddressType type;
    @Column(length = 32)
    private String vat;
    @Column(length = 32)
    private String trn;
    @Column(length = 24)
    private String title;
    @Column(length = 64)
    private String firstName;
    @Column(length = 64)
    private String lastName;
    @Column(length = 64)
    private String street;
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
    @Column(length = 512)
    private String imageHeaderName;
    @Column(length = 64)
    private String imageHeaderType;
    @Column(length = 512)
    private String imageFooterName;
    @Column(length = 64)
    private String imageFooterType;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Builder
    public Address(String id, String name, AddressType type, String vat, String trn, String title,
                   String firstName, String lastName, String street, String zip, String email,
                   String phone, String mobile, String fax, String website, String city,
                   String country, String imageHeaderName, String imageHeaderType,
                   String imageFooterName, String imageFooterType, Company company,
                   Department department, Partner partner) {
        super(id);
        this.name = name;
        this.type = type;
        this.vat = vat;
        this.trn = trn;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.fax = fax;
        this.website = website;
        this.city = city;
        this.country = country;
        this.imageHeaderName = imageHeaderName;
        this.imageHeaderType = imageHeaderType;
        this.imageFooterName = imageFooterName;
        this.imageFooterType = imageFooterType;
        this.company = company;
        this.department = department;
        this.partner = partner;
    }
}
