package cm.gelodia.pm.notification.mapper;

import cm.gelodia.pm.notification.dto.MailTemplateDto;
import cm.gelodia.pm.notification.model.MailTemplate;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {DateTimeMapper.class})
@DecoratedWith(MailTemplateMapperDecorator.class)
public interface MailTemplateMapper {

    MailTemplate map(MailTemplateDto mailTemplateDto);

    MailTemplateDto map(MailTemplate mailTemplate);

    List<MailTemplate> mapToList(List<MailTemplateDto> mailTemplateDtos);

    List<MailTemplateDto> mapToListDto(List<MailTemplate> mailTemplates);
}
