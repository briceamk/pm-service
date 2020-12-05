package cm.gelodia.pm.cron.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.cron.model.JobInfo;
import cm.gelodia.pm.cron.model.JobInfoState;
import cm.gelodia.pm.cron.repository.JobInfoRepository;
import cm.gelodia.pm.cron.service.JobInfoService;
import cm.gelodia.pm.cron.util.JobScheduleCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("JobInfoService")
public class JobInfoServiceImpl implements JobInfoService {

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final JobInfoRepository jobInfoRepository;
    private final ApplicationContext context;
    private final JobScheduleCreator scheduleCreator;

    @Override
    public JobInfo create(UserPrincipal principal, JobInfo jobInfo) {
        if(jobInfoRepository.existsByJobName(jobInfo.getJobName())) {
            log.error("A job with name {} already taken!", jobInfo.getJobName());
            throw new ConflictException(String.format("A job with name %s already taken!", jobInfo.getJobName()));
        }
        if(jobInfoRepository.existsByJobClass(jobInfo.getJobClass())) {
            log.error("A job with class name {} already taken!", jobInfo.getJobName());
            throw new ConflictException(String.format("A job with class name %s already taken!", jobInfo.getJobName()));
        }

        if(jobInfo.getCronExpression() != null && !jobInfo.getCronExpression().isEmpty()) {
            if( CronExpression.isValidExpression(jobInfo.getCronExpression())) {
                jobInfo.setCronJob(true);
            } else {
                log.error("Invalid cron expression {}", jobInfo.getCronExpression());
                throw new BadRequestException(String.format("Invalid cron expression %s", jobInfo.getCronExpression()));
            }
        }

        jobInfo.setState(JobInfoState.DRAFT);
        jobInfo.setCompany(principal.getCompany());
        return jobInfoRepository.save(jobInfo);
    }

    @Override
    public JobInfo update(UserPrincipal principal, JobInfo jobInfo) {
        //TODO validate unique field
        if(jobInfo.getCronExpression() != null && !jobInfo.getCronExpression().isEmpty()) {
            if( CronExpression.isValidExpression(jobInfo.getCronExpression())) {
                jobInfo.setCronJob(true);
            } else {
                log.error("Invalid cron expression {}", jobInfo.getCronExpression());
                throw new BadRequestException(String.format("Invalid cron expression %s", jobInfo.getCronExpression()));
            }
        }
        return jobInfoRepository.save(jobInfo);
    }

    @Override
    public JobInfo findById(UserPrincipal principal, String id) {
        return jobInfoRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Job info with id {} not found", id);
                    throw new ResourceNotFoundException(String.format("Job info with id %s not found", id));
                }
        );
    }

    @Override
    public Page<JobInfo> findAll(UserPrincipal principal, String jobName,
                              String jobGroup, String jobClass, String cronExpression,
                                          Long repeatTime, PageRequest pageRequest) {

        Page<JobInfo> JobInfoPage;

        if (!StringUtils.isEmpty(jobName)) {
            //search by job name
            JobInfoPage = jobInfoRepository.findByJobNameContains(jobName, pageRequest);
        } else if (!StringUtils.isEmpty(jobGroup)) {
            //search by job group
            JobInfoPage = jobInfoRepository.findByJobGroupContains(jobGroup, pageRequest);
        } else if (!StringUtils.isEmpty(jobClass)) {
            //search by job class
            JobInfoPage = jobInfoRepository.findByJobClassContains(jobClass, pageRequest);
        } else if (!StringUtils.isEmpty(cronExpression)) {
            //search by job cron expression
            JobInfoPage = jobInfoRepository.findByCronExpressionContains(cronExpression, pageRequest);
        }
        else if (repeatTime != null) {
            //search by job repeat time
            JobInfoPage = jobInfoRepository.findByRepeatTime(repeatTime, pageRequest);
        }
        else{
            // search all
            JobInfoPage = jobInfoRepository.findAll(pageRequest);
        }

        return JobInfoPage;

    }

    @Override
    public void deleteByIds(UserPrincipal principal, List<String> ids) {
        ids.forEach(id -> jobInfoRepository.delete(findById(principal, id)));
    }

    @Override
    public void startAllSchedulers(UserPrincipal principal) {
        List<JobInfo> jobInfoList = jobInfoRepository.findAll();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        jobInfoList.forEach(jobInfo -> {
            try {
                JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
                        .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();
                if(!scheduler.checkExists(jobDetail.getKey())) {
                    Trigger trigger;
                    jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),
                            false, context, jobInfo.getJobName(), jobInfo.getJobGroup());

                    if(jobInfo.getCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {
                        trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                                jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                    } else {
                        trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
                                jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                    }
                    scheduler.scheduleJob(jobDetail, trigger);
                    jobInfo.setState(JobInfoState.SCHEDULED);
                    update(principal, jobInfo);
                }
            } catch (ClassNotFoundException e) {
                log.error("Class not found - {}", jobInfo.getJobClass(), e);
            } catch (SchedulerException e) {
                log.error("Unable to schedule job {}", jobInfo.getJobName(), e);
            }
        });
    }

    @Override
    public void scheduleNewJob(UserPrincipal principal, JobInfo jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder.newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup()).build();

            if(!scheduler.checkExists(jobDetail.getKey())) {
                Trigger trigger;
                jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()),
                        false, context, jobInfo.getJobName(), jobInfo.getJobGroup());
                if(jobInfo.getCronJob() && CronExpression.isValidExpression(jobInfo.getCronExpression())) {
                    trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                            jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                } else {
                    trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
                            jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
                }
                scheduler.scheduleJob(jobDetail, trigger);
                jobInfo.setState(JobInfoState.SCHEDULED);
                update(principal, jobInfo);
                startScheduleJobNow(principal, jobInfo);
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class not found - {}", jobInfo.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error("Unable to schedule job {}", jobInfo.getJobName(), e);
        }
    }

    @Override
    public void updateScheduleJob(UserPrincipal principal, JobInfo jobInfo) {
        Trigger trigger;
        if(jobInfo.getCronJob()) {
            trigger = scheduleCreator.createCronTrigger(jobInfo.getJobName(), new Date(),
                    jobInfo.getCronExpression(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        } else {
            trigger = scheduleCreator.createSimpleTrigger(jobInfo.getJobName(), new Date(),
                    jobInfo.getRepeatTime(), SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }

        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), trigger);
            jobInfo.setState(JobInfoState.SCHEDULED);
            update(principal, jobInfo);
        } catch (SchedulerException e) {
            log.error("Unable to schedule job {}", jobInfo.getJobName(), e);
        }
    }

    @Override
    public boolean unScheduleJob(UserPrincipal principal, String jobName) {
        try{
            boolean isUnscheduled =  schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName));
            if(isUnscheduled) {
                JobInfo jobInfo = jobInfoRepository.findByJobName(jobName).orElseThrow(
                        () -> {
                            log.error("Job name {} not found", jobName);
                            throw  new ResourceNotFoundException(String.format("Job name {} not found", jobName));
                        }
                );
                jobInfo.setState(JobInfoState.UN_SCHEDULED);
                update(principal, jobInfo);
            }
            return isUnscheduled;
        } catch (SchedulerException e) {
            log.error("Unable to un-schedule job {}", jobName, e);
            throw  new ResourceNotFoundException(String.format("Unable to un-schedule job %s", jobName));
        }
    }

    @Override
    public boolean deleteScheduleJob(UserPrincipal principal, JobInfo jobInfo) {
        try{
            boolean isDeleted = schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            if(isDeleted) {
                jobInfo.setState(JobInfoState.DRAFT);
                update(principal, jobInfo);
            }
            return isDeleted;
        } catch (SchedulerException e) {
            log.error("Unable to delete job {}", jobInfo.getJobName(), e);
            throw  new ResourceNotFoundException(String.format("Unable to delete job %s", jobInfo.getJobName()));
        }
    }

    @Override
    public boolean pauseScheduleJob(UserPrincipal principal, JobInfo jobInfo) {
        try{
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            jobInfo.setState(JobInfoState.PAUSED);
            update(principal, jobInfo);
            return true;
        } catch (SchedulerException e) {
            log.error("Unable to pause job {}", jobInfo.getJobName(), e);
            throw  new ResourceNotFoundException(String.format("Unable to pause job %s", jobInfo.getJobName()));
        }
    }

    @Override
    public boolean resumeScheduleJob(UserPrincipal principal, JobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            jobInfo.setState(JobInfoState.SCHEDULED);
            update(principal, jobInfo);
            return true;
        } catch (SchedulerException e) {
            log.error("Unable to resume job {}", jobInfo.getJobName(), e);
            throw  new ResourceNotFoundException(String.format("Unable to resume job %s", jobInfo.getJobName()));
        }
    }

    @Override
    public boolean startScheduleJobNow(UserPrincipal principal, JobInfo jobInfo) {
        try {
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            jobInfo.setState(JobInfoState.RUNNING);
            update(principal, jobInfo);
            return true;
        } catch (SchedulerException e) {
            log.error("Unable to start job {}", jobInfo.getJobName(), e);
            throw  new ResourceNotFoundException(String.format("Unable to start job %s", jobInfo.getJobName()));
        }
    }

    @Override
    public Optional<JobInfo> findByJobClass(String jobClass) {
        return jobInfoRepository.findByJobClass(jobClass);
    }
}
