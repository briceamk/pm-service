package cm.gelodia.pm.commons.autdit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
@NoArgsConstructor
@JsonIgnoreProperties(value = {"createdAt", "updatedAt","createdBy", "updatedBy"}, allowGetters = false)
public abstract class UserDateAudit extends DateAudit {
    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String updatedBy;

    public UserDateAudit(String id) {
        super(id);
    }

}
