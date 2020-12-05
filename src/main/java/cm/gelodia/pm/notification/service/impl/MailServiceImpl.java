package cm.gelodia.pm.notification.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.notification.constant.NotificationConstantType;
import cm.gelodia.pm.notification.model.*;
import cm.gelodia.pm.notification.repository.MailRepository;
import cm.gelodia.pm.notification.service.MailServerService;
import cm.gelodia.pm.notification.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("mailService")
public class MailServiceImpl implements MailService {

    private final MailRepository mailRepository;
    private final MailServerService mailServerService;


    @Override
    public Mail create(UserPrincipal principal, Mail mail) {

        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }

        if(mail.getState() == null)
            mail.setState(MailState.TO_SEND);

        mail.setCompany(principal.getCompany());
        mail.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        return mailRepository.save(mail);
    }

    @Override
    public Mail update(UserPrincipal principal, Mail mail) {
        return mailRepository.save(mail);
    }

    @Override
    public void send(UserPrincipal principal, Mail mail) {
        // we first check the default mail server
        MailServer mailServer = mailServerService.findByTypeAndDefaultServer(MailServerType.OUT, true);

        JavaMailSender mailSender = mailServerService.getSenderServer(principal, mailServer);


        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);

            mimeMessageHelper.setFrom(mailServer.getUsername());

            mimeMessageHelper.setTo(mail.getEmailTo().split(NotificationConstantType.MAIL_SEPARATOR));

            mimeMessageHelper.setSubject(mail.getSubject());

            mimeMessageHelper.setText(mail.getContent(), true);

            if(mail.getEmailCc() != null && !mail.getEmailCc().isEmpty())
                mimeMessageHelper.setBcc(mail.getEmailCc().split(NotificationConstantType.MAIL_SEPARATOR));

            if(mail.getEmailCci() != null && !mail.getEmailCci().isEmpty())
                mimeMessageHelper.setCc(mail.getEmailCci().split(NotificationConstantType.MAIL_SEPARATOR));
            mailSender.send(message);
            mailServer.setState(MailServerState.ACTIVE);
            mail.setState(MailState.SEND);
            mail.setSendDate(Timestamp.valueOf(LocalDateTime.now()));

        } catch (MessagingException e) {
            log.error("error when sending the mail. invalid parameter!, {}", e.getMessage());
            mail.setState(MailState.SEND_EXCEPTION);
        }
        finally {
            update(principal, mail);
        }
    }

    @Override
    public void sendAll(UserPrincipal principal) {
        List<Mail> mails = mailRepository.findByStateNot(MailState.SEND);

        if (mails != null) mails.forEach(mail -> send(principal, mail));
    }

    @Override
    public Mail findById(UserPrincipal principal, String id) {
        return mailRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Mail with id {} not found!", id);
                    throw new ResourceNotFoundException(String.format("Mail with id %s not found!", id));
                }
        );
    }

    @Override
    public Page<Mail> findAll(UserPrincipal principal, String reference, String state, PageRequest pageRequest) {

        Page<Mail> mailPage;

        if (!StringUtils.isEmpty(reference)) {
            //search by reference
            mailPage = mailRepository.findByReference(reference, pageRequest);
        } else if (!StringUtils.isEmpty(state)) {
            //search by state
            mailPage = mailRepository.findByState(MailState.valueOf(state), pageRequest);
        }
        else{
            // search all
            mailPage = mailRepository.findAll(pageRequest);
        }

        return mailPage;
    }

    @Override
    public List<Mail> findByStateNot(MailState send) {
        return mailRepository.findByStateNot(MailState.SEND);
    }

    @Override
    public void deleteByIds(UserPrincipal principal, List<String> ids) {
        ids.forEach(id -> mailRepository.delete(findById(principal, id)) );
    }

}
