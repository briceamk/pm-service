package cm.gelodia.pm.auth.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_verification_token")
public class VerificationToken extends UserDateAudit {

    private String token;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public VerificationToken(String id, String token, User user) {
        super(id);
        this.token = token;
        this.user = user;
    }
}
