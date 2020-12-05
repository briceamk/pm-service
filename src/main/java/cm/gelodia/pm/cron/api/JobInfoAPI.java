package cm.gelodia.pm.cron.api;

import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.cron.dto.JobInfoDto;
import cm.gelodia.pm.cron.dto.JobInfoDtoPage;
import cm.gelodia.pm.cron.mapper.JobInfoMapper;
import cm.gelodia.pm.cron.model.JobInfo;
import cm.gelodia.pm.cron.service.JobInfoService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cron/job-infos")
@Api(value = "Cron", tags = "Cron End Points")
public class JobInfoAPI {

    private final JobInfoService jobInfoService;
    private final JobInfoMapper jobInfoMapper;
    private final ValidationErrorService validationErrorService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoDto = jobInfoMapper.map(jobInfoService.create(principal, jobInfoMapper.map(jobInfoDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/cron/job-infos/{id}")
                .buildAndExpand(jobInfoDto.getId()).toUri();
        return ResponseEntity.created(uri).body(jobInfoDto);
    }


    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
       jobInfoDto = jobInfoMapper.map( jobInfoService.update(principal, jobInfoMapper.map(jobInfoDto)));

        return ResponseEntity.ok(jobInfoDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findById(@CurrentPrincipal UserPrincipal principal,
                                    @PathVariable String id) {
        return ResponseEntity.ok(jobInfoMapper.map(jobInfoService.findById(principal, id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> findAll(@CurrentPrincipal UserPrincipal principal,
                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "jobName", required = false) String jobName,
                                     @RequestParam(value = "jobGroup", required = false) String jobGroup,
                                     @RequestParam(value = "jobClass", required = false) String jobClass,
                                     @RequestParam(value = "cronExpression", required = false) String cronExpression,
                                     @RequestParam(value = "repeatTime", required = false) Long repeatTime ){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<JobInfo> JobInfoPage = jobInfoService.findAll(principal, jobName, jobGroup, jobClass,
                cronExpression, repeatTime, PageRequest.of(pageNumber, pageSize));

        JobInfoDtoPage jobInfoDtoPage = new JobInfoDtoPage(
                JobInfoPage.getContent().stream().map(jobInfoMapper::map).collect(Collectors.toList()),
                PageRequest.of(JobInfoPage.getPageable().getPageNumber(),
                        JobInfoPage.getPageable().getPageSize()),
                JobInfoPage.getTotalElements()
        );

        return ResponseEntity.ok(jobInfoDtoPage);
    }


    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteById(@CurrentPrincipal UserPrincipal principal,
                                        @PathVariable List<String> ids) {
        jobInfoService.deleteByIds(principal, ids);
        return ResponseEntity.ok(ids);
    }


    @GetMapping("/scheduler/start-all")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> startAllSchedulers(@CurrentPrincipal UserPrincipal principal) {
        jobInfoService.startAllSchedulers(principal);
        return ResponseEntity.ok(new ResponseApi(true, "All jobs has scheduled successfully!"));
    }


    @PutMapping("/scheduler/new")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> scheduleNewJob(@CurrentPrincipal UserPrincipal principal,
                                            @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoService.scheduleNewJob(principal, jobInfoMapper.map(jobInfoDto));
        return ResponseEntity.ok(new ResponseApi(true, "New job has scheduled successfully!"));
    }


    @PutMapping("/scheduler/re")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> reScheduleJob(@CurrentPrincipal UserPrincipal principal,
                                            @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoService.updateScheduleJob(principal, jobInfoMapper.map(jobInfoDto));
        return ResponseEntity.ok(new ResponseApi(true, "job has re-schedule successfully!"));
    }


    @GetMapping("/scheduler/un/{jobName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> unScheduleJob(@CurrentPrincipal UserPrincipal principal,
                                               @PathVariable String jobName) {
        jobInfoService.unScheduleJob(principal, jobName);
        return ResponseEntity.ok(new ResponseApi(true,  "job has un-schedule successfully!"));
    }


    @PutMapping("/scheduler/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteScheduleJob(@CurrentPrincipal UserPrincipal principal,
                                           @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoService.deleteScheduleJob(principal, jobInfoMapper.map(jobInfoDto));
        return ResponseEntity.ok(new ResponseApi(true,   "job schedule delete successfully!"));
    }


    @PutMapping("/scheduler/pause")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> pauseScheduleJob(@CurrentPrincipal UserPrincipal principal,
                                               @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoService.pauseScheduleJob(principal, jobInfoMapper.map(jobInfoDto));
        return ResponseEntity.ok(new ResponseApi(true,   "job schedule pause successfully!"));
    }


    @PutMapping("/scheduler/resume")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> resumeScheduleJob(@CurrentPrincipal UserPrincipal principal,
                                              @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoService.resumeScheduleJob(principal, jobInfoMapper.map(jobInfoDto));
        return ResponseEntity.ok(new ResponseApi(true,    "job schedule resumed successfully!"));
    }


    @PutMapping("/scheduler/start")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> startScheduleJobNow(@CurrentPrincipal UserPrincipal principal,
                                               @Valid @RequestBody JobInfoDto jobInfoDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        jobInfoService.startScheduleJobNow(principal, jobInfoMapper.map(jobInfoDto));
        return ResponseEntity.ok(new ResponseApi(true,    "job started successfully!"));
    }


}
