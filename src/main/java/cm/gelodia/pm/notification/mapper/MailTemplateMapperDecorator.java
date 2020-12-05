package cm.gelodia.pm.notification.mapper;

import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.notification.dto.MailTemplateDto;
import cm.gelodia.pm.notification.model.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MailTemplateMapperDecorator implements MailTemplateMapper {
	
	private MailTemplateMapper mailTemplateMapper;
	private CompanyMapper companyMapper;
	
	
	@Autowired
	public void setMailMapper(MailTemplateMapper mailTemplateMapper) {
		this.mailTemplateMapper = mailTemplateMapper;
	}
	
	@Autowired
	public void setCompanyMapper(CompanyMapper companyMapper) {
		this.companyMapper = companyMapper;
	}


	@Override
	public MailTemplate map(MailTemplateDto mailTemplateDto) {
		MailTemplate mailTemplate = mailTemplateMapper.map(mailTemplateDto);
		if(mailTemplateDto.getCompanyDto() != null)
			mailTemplate.setCompany(companyMapper.map(mailTemplateDto.getCompanyDto()));
		return mailTemplate;
	}

	@Override
	public MailTemplateDto map(MailTemplate mailTemplate) {
		MailTemplateDto mailTemplateDto = mailTemplateMapper.map(mailTemplate);
		if(mailTemplate.getCompany() !=  null)
			mailTemplateDto.setCompanyDto(companyMapper.map(mailTemplate.getCompany()));
		return mailTemplateDto;
	}

	@Override
	public List<MailTemplate> mapToList(List<MailTemplateDto> mailTemplateDtos) {
		return mailTemplateDtos.stream().map(mailTemplateMapper::map).collect(Collectors.toList());
	}


	@Override
	public List<MailTemplateDto> mapToListDto(List<MailTemplate> mailTemplates) {
		return mailTemplates.stream().map(mailTemplateMapper::map).collect(Collectors.toList());
	}

}
