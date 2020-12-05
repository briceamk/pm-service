package cm.gelodia.pm.cron.mapper;

import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.cron.dto.JobInfoDto;
import cm.gelodia.pm.cron.model.JobInfo;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobInfoMapperDecorator implements JobInfoMapper{

    private JobInfoMapper jobInfoMapper;
    private CompanyMapper companyMapper;

    @Autowired
    public void setJobInfoMapper(JobInfoMapper jobInfoMapper) {
        this.jobInfoMapper = jobInfoMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public JobInfo map(JobInfoDto jobInfoDto) {
        JobInfo jobInfo = jobInfoMapper.map(jobInfoDto);
        if(jobInfoDto.getCompanyDto() != null)
            jobInfo.setCompany(companyMapper.map(jobInfoDto.getCompanyDto()));
        return jobInfo;
    }

    @Override
    public JobInfoDto map(JobInfo jobInfo) {
        JobInfoDto jobInfoDto = jobInfoMapper.map(jobInfo);
        if(jobInfo.getCompany() != null)
            jobInfoDto.setCompanyDto(companyMapper.map(jobInfo.getCompany()));
        return jobInfoDto;
    }
}
