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
@Table(name = "notification_mail_servers")
public class MailServer extends UserDateAudit {

    static final long serialVersionUID = -1417063325310358804L;
    @Column(nullable = false)
    private String hostname;
    @Column(nullable = false)
    private String port;
    private String username;
    private String password;
    private Boolean enableSSL;
    private Boolean enableAuth;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailServerType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailServerState state;
    private Boolean defaultServer;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public MailServer(String id,String hostname, String port, String username, String password, Boolean enableSSL,
                      Boolean enableAuth, MailServerType type,  MailServerState state, Boolean defaultServer, Company company) {
        super(id);
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.enableSSL = enableSSL;
        this.enableAuth = enableAuth;
        this.type = type;
        this.state = state;
        this.defaultServer = defaultServer;
        this.company = company;
    }
}
