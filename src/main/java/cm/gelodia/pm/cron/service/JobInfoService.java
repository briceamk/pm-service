package cm.gelodia.pm.cron.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.cron.model.JobInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface JobInfoService {


    JobInfo create(UserPrincipal principal, JobInfo jobInfo);

    JobInfo update(UserPrincipal principal, JobInfo JobInfo);

    JobInfo findById(UserPrincipal principal, String id);

    Page<JobInfo> findAll(UserPrincipal principal, String jobName, String jobGroup,
                       String jobClass, String cronExpression, Long repeatTime, PageRequest pageRequest);

    void deleteByIds(UserPrincipal principal, List<String> ids);

    void startAllSchedulers(UserPrincipal principal);

    void scheduleNewJob(UserPrincipal principal, JobInfo jobInfo);

    void updateScheduleJob(UserPrincipal principal, JobInfo jobInfo);

    boolean unScheduleJob(UserPrincipal principal, String jobName);

    boolean deleteScheduleJob(UserPrincipal principal, JobInfo jobInfo);

    boolean pauseScheduleJob(UserPrincipal principal, JobInfo jobInfo);

    boolean resumeScheduleJob(UserPrincipal principal, JobInfo jobInfo);

    boolean startScheduleJobNow(UserPrincipal principal, JobInfo jobInfo);

    Optional<JobInfo> findByJobClass(String jobClass);
}
