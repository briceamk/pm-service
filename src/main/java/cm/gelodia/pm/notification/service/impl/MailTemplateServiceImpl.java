package cm.gelodia.pm.notification.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.notification.model.MailTemplate;
import cm.gelodia.pm.notification.model.MailTemplateType;
import cm.gelodia.pm.notification.repository.MailTemplateRepository;
import cm.gelodia.pm.notification.service.MailTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("mailTemplateService")
public class MailTemplateServiceImpl implements MailTemplateService {

    private final MailTemplateRepository mailTemplateRepository;

    @Override
    public MailTemplate create(UserPrincipal principal, MailTemplate mailTemplate) {

        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }

        if(mailTemplateRepository.existsByName(mailTemplate.getName())) {
            log.error("A template with name {} exists!", mailTemplate.getName());
            throw  new ConflictException(String.format("A template with name %s exists!", mailTemplate.getName()));
        }

        mailTemplate.setCompany(principal.getCompany());
        return mailTemplateRepository.save(mailTemplate);
    }

    @Override
    public MailTemplate update(UserPrincipal principal, MailTemplate mailTemplate) {
        //todo validate unique field
        return mailTemplateRepository.save(mailTemplate);
    }

    @Override
    public MailTemplate findById(UserPrincipal principal, String id) {
        return mailTemplateRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Mail template with id {} not found", id);
                    throw new ResourceNotFoundException(String.format("Mail template with id %s not found", id));
                }
        );
    }

    @Override
    public MailTemplate findByName(UserPrincipal principal, String name) {
        return mailTemplateRepository.findByName(name).orElseThrow(
                () -> {
                    log.error("Mail template with name {} not found", name);
                    throw new ResourceNotFoundException(String.format("Mail template with name %s not found", name));
                }
        );
    }

    @Override
    public MailTemplate findByType(UserPrincipal principal, MailTemplateType type) {
        return mailTemplateRepository.findByType(type).orElseThrow(
                () -> {
                    log.error("Mail template for type {} not found", type.name());
                    throw new ResourceNotFoundException(String.format("Mail template for type %s not found", type.name()));
                }
        );
    }

    @Override
    public Page<MailTemplate> findAll(UserPrincipal principal, String name, PageRequest pageRequest) {

        Page<MailTemplate> mailTemplatePage;

        if (!StringUtils.isEmpty(name)) {
            //search by name
            mailTemplatePage = mailTemplateRepository.findByNameContains(name, pageRequest);
        }
        else{
            // search all
            mailTemplatePage = mailTemplateRepository.findAll(pageRequest);
        }

        return mailTemplatePage;
    }

    @Override
    public void deleteByIds(UserPrincipal principal, List<String> ids) {
        ids.forEach(id -> mailTemplateRepository.delete(findById(principal, id)));
    }
}
