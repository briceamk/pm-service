package cm.gelodia.pm.notification.service;


import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.notification.model.Mail;
import cm.gelodia.pm.notification.model.MailState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;


public interface MailService {
    Mail create(UserPrincipal principal, Mail mail);

    Mail update(UserPrincipal principal, Mail mail);

    void send(UserPrincipal principal, Mail mail);

    void sendAll(UserPrincipal principal);

    Mail findById(UserPrincipal principal, String id);

    Page<Mail> findAll(UserPrincipal principal, String reference, String state, PageRequest pageRequest);

    List<Mail> findByStateNot(MailState send);

    void deleteByIds(UserPrincipal principal, List<String> ids);
}
