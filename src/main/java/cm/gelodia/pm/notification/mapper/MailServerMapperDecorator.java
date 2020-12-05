package cm.gelodia.pm.notification.mapper;


import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.notification.dto.MailServerDto;
import cm.gelodia.pm.notification.model.MailServer;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class MailServerMapperDecorator implements MailServerMapper {
	
	private MailServerMapper mailServerMapper;
	private CompanyMapper companyMapper;
	
	
	@Autowired
	public void setMailMapper(MailServerMapper mailServerMapper) {
		this.mailServerMapper = mailServerMapper;
	}
	
	@Autowired
	public void setCompanyMapper(CompanyMapper companyMapper) {
		this.companyMapper = companyMapper;
	}


	@Override
	public MailServerDto map(MailServer mailServer) {
		MailServerDto mailServerDto = mailServerMapper.map(mailServer);
		if(mailServer.getCompany() !=  null)
			mailServerDto.setCompanyDto(companyMapper.map(mailServer.getCompany()));
		return mailServerDto;
	}

	@Override
	public MailServer map(MailServerDto mailServerDto) {
		MailServer mailServer = mailServerMapper.map(mailServerDto);
		if(mailServerDto.getCompanyDto() != null)
			mailServer.setCompany(companyMapper.map(mailServerDto.getCompanyDto()));
		return mailServer;
	}
}
