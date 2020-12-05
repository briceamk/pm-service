package cm.gelodia.pm.cron.repository;

import cm.gelodia.pm.cron.model.JobInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, String> {
    Boolean existsByJobName(String jobName);

    Boolean existsByJobClass(String jobClass);

    Page<JobInfo> findByJobNameContains(String jobName, Pageable pageable);

    Page<JobInfo> findByJobGroupContains(String jobGroup, Pageable pageable);

    Page<JobInfo> findByJobClassContains(String jobClass, Pageable pageable);

    Page<JobInfo> findByCronExpressionContains(String cronExpression, Pageable pageable);

    Page<JobInfo> findByRepeatTime(Long repeatTime, Pageable pageable);

    Optional<JobInfo> findByJobName(String jobName);

    Optional<JobInfo> findByJobClass(String jobClass);
}
