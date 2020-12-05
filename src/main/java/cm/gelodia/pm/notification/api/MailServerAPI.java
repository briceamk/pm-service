package cm.gelodia.pm.notification.api;

import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.notification.dto.MailServerDto;
import cm.gelodia.pm.notification.dto.MailServerDtoPage;
import cm.gelodia.pm.notification.mapper.MailServerMapper;
import cm.gelodia.pm.notification.model.MailServer;
import cm.gelodia.pm.notification.service.MailServerService;
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

import javax.script.ScriptException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification/mail-servers")
@Api(value = "Mail Server", tags = "Mail Server End Points")
public class MailServerAPI {


    private final MailServerService mailServerService;
    private final MailServerMapper mailServerMapper;
    private final ValidationErrorService validationErrorService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> create(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody MailServerDto mailServerDto, BindingResult result)  {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailServerDto = mailServerMapper.map(mailServerService.create(principal, mailServerMapper.map(mailServerDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/mail-servers/{id}")
                .buildAndExpand(mailServerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(mailServerDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> update(@CurrentPrincipal UserPrincipal principal,
                                    @Validated @RequestBody MailServerDto mailServerDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
       mailServerDto = mailServerMapper.map(mailServerService.update(principal, mailServerMapper.map(mailServerDto)));
        return new ResponseEntity<>(mailServerDto, HttpStatus.OK);
    }

    @PutMapping("/test")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> testServer(@CurrentPrincipal UserPrincipal principal,
                                    @Validated @RequestBody MailServerDto mailServerDto, BindingResult result) throws ScriptException {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailServerService.testServer(principal, mailServerMapper.map(mailServerDto));
        return new ResponseEntity<>(new ResponseApi(true, "Server is connected successfully!"), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> findById(@CurrentPrincipal UserPrincipal principal,
                                      @PathVariable String id) {
        return new ResponseEntity<>(mailServerMapper.map(mailServerService.findById(principal, id)), HttpStatus.OK);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> findAll(@CurrentPrincipal UserPrincipal principal,
                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "hostname", required = false) String hostname){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<MailServer> mailServerPage =  mailServerService.findAll(principal, hostname, PageRequest.of(pageNumber, pageSize));

        MailServerDtoPage mailServerDtoPage = new MailServerDtoPage(
                mailServerPage.getContent().stream().map(mailServerMapper::map).collect(Collectors.toList()),
                PageRequest.of(mailServerPage.getPageable().getPageNumber(),
                        mailServerPage.getPageable().getPageSize()),
                mailServerPage.getTotalElements()
        );

        return new ResponseEntity<>(mailServerDtoPage, HttpStatus.OK);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> deleteByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        mailServerService.deleteByIds(principal, ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }


}
