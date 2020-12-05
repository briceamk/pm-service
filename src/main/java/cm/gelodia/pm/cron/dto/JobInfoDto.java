package cm.gelodia.pm.cron.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfoDto {

    static final long serialVersionUID = -7321178669475570500L;

    private String id;
    @NotEmpty(message = "Job name is required")
    private String jobName;
    @NotEmpty(message = "Job group name is required")
    private String jobGroup;
    @NotEmpty(message = "Class name is required")
    private String jobClass;
    private String cronExpression;
    private Long repeatTime;
    private Boolean cronJob;
    private String state;
    private CompanyDto companyDto;
}
