package cm.gelodia.pm.boostrap;

import cm.gelodia.pm.cron.service.JobInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerStartUpHandler implements ApplicationRunner {

    private final JobInfoService jobInfoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Schedule All new scheduler jobs at app startup - starting");
        try{
            jobInfoService.startAllSchedulers(null);
            log.info("Schedule all new jobs at startup complete - complete");
        } catch (Exception e) {
            log.error("Schedule all new scheduler jobs at app startup - error", e);
        }
    }
}
