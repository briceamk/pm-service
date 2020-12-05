package cm.gelodia.pm.cron.mapper;

import cm.gelodia.pm.cron.dto.JobInfoDto;
import cm.gelodia.pm.cron.model.JobInfo;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(JobInfoMapperDecorator.class)
public interface JobInfoMapper {
    JobInfo map(JobInfoDto jobInfoDto);
    JobInfoDto map(JobInfo jobInfo);
}
