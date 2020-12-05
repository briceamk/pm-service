package cm.gelodia.pm.notification.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.notification.model.MailTemplate;
import cm.gelodia.pm.notification.model.MailTemplateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface MailTemplateService {
    MailTemplate create(UserPrincipal principal, MailTemplate mailTemplate);

    MailTemplate update(UserPrincipal principal, MailTemplate mailTemplate);

    MailTemplate findById(UserPrincipal principal, String id);

    MailTemplate findByName(UserPrincipal principal, String name);

    MailTemplate findByType(UserPrincipal principal, MailTemplateType type);

    Page<MailTemplate> findAll(UserPrincipal principal, String name, PageRequest pageRequest);

    void deleteByIds(UserPrincipal principal, List<String> ids);
}
