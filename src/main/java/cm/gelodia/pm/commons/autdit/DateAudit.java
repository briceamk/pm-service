package cm.gelodia.pm.commons.autdit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties( value = {"createdAt", "updatedAt"}, allowGetters = false)
public abstract class DateAudit implements Serializable {



    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(strategy = "uuid2", name = "uuid2")
    protected String id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    protected LocalDateTime updatedAt;

    public DateAudit(String id) {
        this.id = id;
    }
}
