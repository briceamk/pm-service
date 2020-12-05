package cm.gelodia.pm.notification.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notification_mail_templates")
public class MailTemplate extends UserDateAudit {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private MailTemplateType type;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public MailTemplate(String id, String name, String subject, String content, MailTemplateType type, Company company) {
        super(id);
        this.name = name;
        this.subject = subject;
        this.content = content;
        this.type = type;
        this.company = company;
    }
}
