package cm.gelodia.pm.cron.job;


import cm.gelodia.pm.cron.model.JobInfoState;
import cm.gelodia.pm.cron.service.JobInfoService;
import cm.gelodia.pm.notification.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@Slf4j
@DisallowConcurrentExecution
public class SendMailCronJob extends QuartzJobBean {

    @Autowired
    private MailService mailService;
     @Autowired
     private JobInfoService jobInfoService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        jobInfoService.findByJobClass(context.getJobDetail().getJobClass().toString().replace("class ", "")).ifPresent(jobInfo -> {
            jobInfo.setState(JobInfoState.RUNNING);
            jobInfoService.update(null, jobInfo);
        });


        mailService.sendAll(null);

        log.info("SendMail Cron Job End................");
    }
}
