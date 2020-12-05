package cm.gelodia.pm.notification.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.notification.model.MailServer;
import cm.gelodia.pm.notification.model.MailServerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;


public interface MailServerService {
    MailServer create(UserPrincipal principal, MailServer mailServer);

    MailServer update(UserPrincipal principal, MailServer mailServer);

    void testServer(UserPrincipal principal, MailServer map);

    MailServer findById(UserPrincipal principal, String id);

    Page<MailServer> findAll(UserPrincipal principal, String hostname, PageRequest pageRequest);

    JavaMailSender getSenderServer(UserPrincipal principal, MailServer mailServer);

    MailServer findByTypeAndDefaultServer(MailServerType out, boolean b);

    void deleteByIds(UserPrincipal principal, List<String> ids);
}
