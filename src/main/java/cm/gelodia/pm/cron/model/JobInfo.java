package cm.gelodia.pm.cron.model;

import cm.gelodia.pm.commons.autdit.UserDateAudit;
import cm.gelodia.pm.company.model.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cron_job_infos")
public class JobInfo extends UserDateAudit {

    static final long serialVersionUID = 5597058793100819223L;
    @Column(nullable = false, unique = true)
    private String jobName;
    @Column(nullable = false)
    private String jobGroup;
    @Column(nullable = false, unique = true)
    private String jobClass;
    private String cronExpression;
    private Long repeatTime;
    private Boolean cronJob;
    @Enumerated(EnumType.STRING)
    private JobInfoState state;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    public JobInfo(String id, String jobName, String jobGroup, String jobClass,
                   String cronExpression, Long repeatTime, Boolean cronJob, JobInfoState state,
             Company company) {
        super(id);
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.jobClass = jobClass;
        this.cronExpression = cronExpression;
        this.repeatTime = repeatTime;
        this.cronJob = cronJob;
        this.state = state;
        this.company = company;
    }
}
