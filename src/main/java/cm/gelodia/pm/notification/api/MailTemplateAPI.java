package cm.gelodia.pm.notification.api;


import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.notification.dto.MailTemplateDto;
import cm.gelodia.pm.notification.dto.MailTemplateDtoPage;
import cm.gelodia.pm.notification.mapper.MailTemplateMapper;
import cm.gelodia.pm.notification.model.MailTemplate;
import cm.gelodia.pm.notification.service.MailTemplateService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification/mail-templates")
@Api(value = "Mail Template", tags = "Mail Template End Points")
public class MailTemplateAPI {


    private final MailTemplateService mailTemplateService;
    private final MailTemplateMapper mailTemplateMapper;
    private final ValidationErrorService validationErrorService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> create(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody MailTemplateDto mailTemplateDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailTemplateDto = mailTemplateMapper.map(mailTemplateService.create(principal, mailTemplateMapper.map(mailTemplateDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/mail-templates/{id}")
                .buildAndExpand(mailTemplateDto.getId()).toUri();
        return ResponseEntity.created(uri).body(mailTemplateDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> update(@CurrentPrincipal UserPrincipal principal,
                                    @Validated @RequestBody MailTemplateDto mailTemplateDto, BindingResult result)  {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailTemplateDto = mailTemplateMapper.map(mailTemplateService.update(principal, mailTemplateMapper.map(mailTemplateDto)));
        return new ResponseEntity<>(mailTemplateDto, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> findById(@CurrentPrincipal UserPrincipal principal,
                                      @PathVariable String id) {
        return new ResponseEntity<>(mailTemplateMapper.map(mailTemplateService.findById(principal, id)), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> findByName(@CurrentPrincipal UserPrincipal principal,
                                      @PathVariable String name) {
        return new ResponseEntity<>(mailTemplateMapper.map(mailTemplateService.findByName(principal, name)), HttpStatus.OK);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> findAll(@CurrentPrincipal UserPrincipal principal,
                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "name", required = false) String name){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<MailTemplate> mailTemplatePage =  mailTemplateService.findAll(principal, name, PageRequest.of(pageNumber, pageSize));

        MailTemplateDtoPage mailTemplateDtoPage = new MailTemplateDtoPage(
                mailTemplatePage.getContent().stream().map(mailTemplateMapper::map).collect(Collectors.toList()),
                PageRequest.of(mailTemplatePage.getPageable().getPageNumber(),
                        mailTemplatePage.getPageable().getPageSize()),
                mailTemplatePage.getTotalElements()
        );

        return new ResponseEntity<>(mailTemplateDtoPage, HttpStatus.OK);
    }

    @DeleteMapping("/many/{ids}")
    public ResponseEntity<?> deleteByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        mailTemplateService.deleteByIds(principal, ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }


}
