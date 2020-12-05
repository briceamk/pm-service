package cm.gelodia.pm.notification.mapper;

import cm.gelodia.pm.notification.dto.MailDto;
import cm.gelodia.pm.notification.model.Mail;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {DateTimeMapper.class})
@DecoratedWith(MailMapperDecorator.class)
public interface MailMapper {

    Mail map(MailDto mailDto);

    MailDto map(Mail mail);

    List<Mail> mapToList(List<MailDto> mailDtos);

    List<MailDto> mapToListDto(List<Mail> mails);
}
