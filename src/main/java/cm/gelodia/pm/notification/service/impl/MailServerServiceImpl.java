package cm.gelodia.pm.notification.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.AppException;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.notification.constant.NotificationConstantType;
import cm.gelodia.pm.notification.model.MailServer;
import cm.gelodia.pm.notification.model.MailServerState;
import cm.gelodia.pm.notification.model.MailServerType;
import cm.gelodia.pm.notification.repository.MailServerRepository;
import cm.gelodia.pm.notification.service.MailServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Service("mailServerService")
public class MailServerServiceImpl implements MailServerService {

    private final MailServerRepository mailServerRepository;


    @Override
    public MailServer create(UserPrincipal principal, MailServer mailServer) {

        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }

        if(mailServerRepository.existsByHostnameAndType(mailServer.getHostname(), mailServer.getType())) {
            log.error("the hostname {} for type {} is already taken!", mailServer.getHostname(), mailServer.getType().name());
            throw  new BadRequestException(String.format("the hostname %s for type %s is already taken!", mailServer.getHostname(), mailServer.getType().name()));
        }
        mailServer.setCompany(principal.getCompany());

        mailServer = validateServer(mailServer);
        mailServer.setState(MailServerState.DRAFT);

        return mailServerRepository.save(mailServer);
    }




    @Override
    public MailServer update(UserPrincipal principal, MailServer mailServer) {
        //todo validate unique hostname and type
        mailServer = validateServer(mailServer);
        return mailServerRepository.save(mailServer);
    }

    @Override
    public void testServer(UserPrincipal principal, MailServer mailServer)  {
        JavaMailSender mailSender = getSenderServer(principal, mailServer);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailServer.getUsername());
        message.setTo(NotificationConstantType.DEFAULT_EMAIL_TEST);
        message.setSubject("This is object of test");
        message.setText("This is the content of test");

        try {
            mailSender.send(message);
            mailServer.setState(MailServerState.ACTIVE);
            mailServerRepository.save(mailServer);

        } catch (Exception e) {
            log.error("error when sending the mail. invalid parameter!, {}", e.getMessage());
            throw new AppException("Parameter are not valid " + e.getMessage());
        }
    }

    @Override
    public MailServer findById(UserPrincipal principal, String id) {
        return mailServerRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Server mail with id {} not found", id);
                    throw new ResourceNotFoundException(String.format("Server mail with id %s not found", id));
                }
        );
    }

    @Override
    public Page<MailServer> findAll(UserPrincipal principal, String hostname, PageRequest pageRequest) {

        Page<MailServer> mailServerPage;

        if (!StringUtils.isEmpty(hostname)) {
            //search by hostname
            mailServerPage = mailServerRepository.findByHostnameContains(hostname, pageRequest);
        }
        else{
            // search all
            mailServerPage = mailServerRepository.findAll(pageRequest);
        }

        return mailServerPage;
    }

    @Override
    public JavaMailSender getSenderServer(UserPrincipal principal, MailServer mailServer) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailServer.getHostname());
        mailSender.setPort(Integer.parseInt(mailServer.getPort()));

        if (mailServer.getEnableAuth()) {
            mailSender.setUsername(mailServer.getUsername());
            mailSender.setPassword(mailServer.getPassword());
        }

        Properties properties = new Properties();

        properties.put(NotificationConstantType.EMAIL_TRANSPORT_PROTOCOL_KEY,"smtp");
        properties.put(NotificationConstantType.EMAIL_AUTH_KEY,mailServer.getEnableAuth().toString());
        properties.put(NotificationConstantType.EMAIL_START_TLS_ENABLE, mailServer.getEnableSSL().toString());

        mailSender.setDefaultEncoding("UTF-8");

        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }

    @Override
    public MailServer findByTypeAndDefaultServer(MailServerType out, boolean b) {
        return mailServerRepository.findByTypeAndDefaultServer(MailServerType.OUT, true).orElseThrow(
                () -> {
                    log.error("No default mail sender server found");
                    throw new ResourceNotFoundException("No default mail sender server found");
                }
        );
    }

    @Override
    public void deleteByIds(UserPrincipal principal, List<String> ids) {
        ids.forEach(id -> mailServerRepository.delete(findById(principal, id)) );
    }


    private MailServer validateServer(MailServer mailServer) {

        if(mailServer.getEnableAuth() == null) {
            mailServer.setEnableAuth(false);
        }

        if(mailServer.getEnableAuth()) {
            if(mailServer.getType().equals(MailServerType.OUT)) {
                if(mailServer.getUsername() == null || mailServer.getUsername().isEmpty()) {
                    log.error("username is required!");
                    throw new BadRequestException("username is required!");
                }

                if(mailServer.getPassword() == null || mailServer.getPassword().isEmpty()) {
                    log.error("password is required!");
                    throw new BadRequestException("password is required!");
                }
            }
        }



        if(mailServer.getEnableSSL() == null) {
            mailServer.setEnableSSL(false);
        }

        if(mailServer.getDefaultServer() == null) {
            mailServer.setDefaultServer(false);
        }
        // if server to create is set as default we find a existing default server and uncheck it as default
        if(mailServer.getDefaultServer()) {
            MailServer currentDefaultMailServer = mailServerRepository.findByTypeAndDefaultServer(mailServer.getType(), true).orElse(null);
            if(currentDefaultMailServer != null) {
                currentDefaultMailServer.setDefaultServer(false);
                mailServerRepository.save(currentDefaultMailServer);
            }
        }
        //if current server is not set as default we verify if is there a default server. if not we set server to create as default
        else {
            if(mailServerRepository.countByTypeAndDefaultServer(mailServer.getType(), true) <= 0) {
                mailServer.setDefaultServer(true);
            }
        }
        return mailServer;
    }
}
