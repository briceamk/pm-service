package cm.gelodia.pm.notification.mapper;

import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.notification.dto.MailDto;
import cm.gelodia.pm.notification.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MailMapperDecorator implements MailMapper {
	
	private MailMapper mailMapper;
	private CompanyMapper companyMapper;
	
	
	@Autowired
	public void setMailMapper(MailMapper mailMapper) {
		this.mailMapper = mailMapper;
	}
	
	@Autowired
	public void setCompanyMapper(CompanyMapper companyMapper) {
		this.companyMapper = companyMapper;
	}


	@Override
	public Mail map(MailDto mailDto) {
		Mail mail = mailMapper.map(mailDto);
		if(mailDto.getCompanyDto() != null)
			mail.setCompany(companyMapper.map(mailDto.getCompanyDto()));
		return mail;
	}

	@Override
	public MailDto map(Mail mail) {
		MailDto mailDto = mailMapper.map(mail);
		if(mail.getCompany() !=  null)
			mailDto.setCompanyDto(companyMapper.map(mail.getCompany()));
		return mailDto;
	}

	@Override
	public List<Mail> mapToList(List<MailDto> mailDtos) {
		return mailDtos.stream().map(mailMapper::map).collect(Collectors.toList());
	}


	@Override
	public List<MailDto> mapToListDto(List<Mail> mails) {
		return mails.stream().map(mailMapper::map).collect(Collectors.toList());
	}

}
