package cm.gelodia.pm.notification.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notification_mails")
public class Mail extends UserDateAudit {

    static final long serialVersionUID = 3913125620645474768L;

    @Column(nullable = false)
    private String subject;
    @Column(nullable = false)
    private String emailTo;
    private String emailCc;
    private String emailCci;
    @Column(columnDefinition = "text")
    private String content;
    private String relatedClass;
    private String relatedObjectId;
    private String reference;
    private Timestamp sendDate;
    private Timestamp creationDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailState state;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public Mail(String id, String subject, String emailTo, String emailCc, String emailCci, String content,
                String relatedClass, String relatedObjectId, String reference, Timestamp sendDate,
                Timestamp creationDate, MailState state, Company company) {
        super(id);
        this.subject = subject;
        this.emailTo = emailTo;
        this.emailCc = emailCc;
        this.emailCci = emailCci;
        this.content = content;
        this.relatedClass = relatedClass;
        this.relatedObjectId = relatedObjectId;
        this.reference = reference;
        this.creationDate = creationDate;
        this.sendDate = sendDate;
        this.state = state;
        this.company = company;
    }
}
