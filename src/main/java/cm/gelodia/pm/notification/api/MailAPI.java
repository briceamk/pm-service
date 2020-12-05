package cm.gelodia.pm.notification.api;

import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.notification.dto.MailDto;
import cm.gelodia.pm.notification.dto.MailDtoPage;
import cm.gelodia.pm.notification.mapper.MailMapper;
import cm.gelodia.pm.notification.model.Mail;
import cm.gelodia.pm.notification.service.MailService;
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
@RequestMapping("/api/v1/notification/mails")
@Api(value = "Mail", tags = "Mail End Points")
public class MailAPI {


    private final MailService mailService;
    private final MailMapper mailMapper;
    private final ValidationErrorService validationErrorService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> create(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody MailDto mailDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailDto = mailMapper.map(mailService.create(principal, mailMapper.map(mailDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/mails/{id}")
                .buildAndExpand(mailDto.getId()).toUri();
        return ResponseEntity.created(uri).body(mailDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<?> update(@CurrentPrincipal UserPrincipal principal,
                                    @Validated @RequestBody MailDto mailDto, BindingResult result)  {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailDto = mailMapper.map(mailService.update(principal, mailMapper.map(mailDto)));
        return new ResponseEntity<>(mailDto, HttpStatus.OK);
    }

    @PutMapping("/send")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> send(@CurrentPrincipal UserPrincipal principal,
                                    @Validated @RequestBody MailDto mailDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailService.send(principal, mailMapper.map(mailDto));
        return new ResponseEntity<>(new ResponseApi(true, "Mail send successfully!"), HttpStatus.OK);
    }

    @PutMapping("/send/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> sendAll(@CurrentPrincipal UserPrincipal principal,
                                      BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        mailService.sendAll(principal);
        return new ResponseEntity<>(new ResponseApi(true, "Mails send successfully!"), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> findById(@CurrentPrincipal UserPrincipal principal,
                                      @PathVariable String id) {
        return new ResponseEntity<>(mailMapper.map(mailService.findById(principal, id)), HttpStatus.OK);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> findAll(@CurrentPrincipal UserPrincipal principal,
                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "reference", required = false) String reference,
                                     @RequestParam(value = "state", required = false) String state){

        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Mail> mailPage =  mailService.findAll(principal, reference, state, PageRequest.of(pageNumber, pageSize));

        MailDtoPage mailDtoPage = new MailDtoPage(
                mailPage.getContent().stream().map(mailMapper::map).collect(Collectors.toList()),
                PageRequest.of(mailPage.getPageable().getPageNumber(),
                        mailPage.getPageable().getPageSize()),
                mailPage.getTotalElements()
        );

        return new ResponseEntity<>(mailDtoPage, HttpStatus.OK);
    }


    @DeleteMapping("/many/{ids}")
    public ResponseEntity<?> deleteByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        mailService.deleteByIds(principal, ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }


}
