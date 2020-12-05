package cm.gelodia.pm.notification.dto;

import cm.gelodia.pm.company.dto.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailDto implements Serializable {

    static final long serialVersionUID = -2492778422982982192L;

    private String id;
    @NotEmpty(message = "subject is required!")
    private String subject;
    @NotEmpty(message = "recipient is required!")
    private String emailTo;
    private String emailCc;
    private String emailCci;
    private String content;
    private String relatedClass;
    private String relatedObjectId;
    private String reference;
    private LocalDateTime creationDate;
    private LocalDateTime sendDate;
    private String state;
    private CompanyDto companyDto;
}
