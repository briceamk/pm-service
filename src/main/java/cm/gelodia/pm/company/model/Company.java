package cm.gelodia.pm.company.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company_companies")
public class Company extends UserDateAudit {
    @Column(length = 5, nullable = false)
    private String code;
    @Column(length = 64, nullable = false)
    private String name;
    @Column(length = 16)
    private String phoneNumber;
    @Column(length = 16)
    private String mobileNumber;
    @Column(length = 128)
    private String email;
    @Column(length = 64)
    private String city;
    @Column(length = 10)
    private String zip;
    @Column(length = 128)
    private String street;
    @Column(length = 64)
    private String country;
    @Column(length = 32)
    private String vat;
    @Column(length = 32)
    private String trn;
    private Boolean active;
    private String imageName;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @JsonIgnore
    private byte[] image;
    private String imageType;


    @Builder
    public Company(String id,  String code, String name, String phoneNumber, String mobileNumber, String email, String city, String zip,
                   String street, String country, String vat, String trn, String imageName, Boolean active, byte[] image, String imageType) {
        super(id);
        this.code = code;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.city = city;
        this.zip = zip;
        this.street = street;
        this.country = country;
        this.vat = vat;
        this.trn = trn;
        this.imageName = imageName;
        this.active = active;
        this.image = image;
        this.imageType = imageType;
    }
}
