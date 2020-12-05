package cm.gelodia.pm.notification.mapper;


import cm.gelodia.pm.notification.dto.MailServerDto;
import cm.gelodia.pm.notification.model.MailServer;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;


@Mapper
@DecoratedWith(MailServerMapperDecorator.class)
public interface MailServerMapper {

    MailServer map(MailServerDto mailServerDto);

    MailServerDto map(MailServer mailServer);
}
