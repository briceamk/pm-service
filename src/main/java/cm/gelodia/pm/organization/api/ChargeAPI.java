package cm.gelodia.pm.organization.api;

import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.organization.dto.ChargeDto;
import cm.gelodia.pm.organization.dto.ChargeDtoPage;
import cm.gelodia.pm.organization.mapper.ChargeMapper;
import cm.gelodia.pm.organization.model.Charge;
import cm.gelodia.pm.organization.service.ChargeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization/charges")
@Api(value = "Charge", tags = "Charge End Points")
public class ChargeAPI {
    private final ChargeService chargeService;
    private final ChargeMapper chargeMapper;
    private final ValidationErrorService validationErrorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> createCharge(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody ChargeDto chargeDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        chargeDto = chargeMapper.map(chargeService.create(principal, chargeMapper.map(chargeDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/organization/charges/{id}")
                .buildAndExpand(chargeDto.getId()).toUri();
        return ResponseEntity.created(uri).body(chargeDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> updateCharge(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody ChargeDto chargeDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return ResponseEntity.ok(chargeMapper.map(chargeService.update(principal, chargeMapper.map(chargeDto))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getCompanyByPage(@CurrentPrincipal UserPrincipal principal,
                                              @RequestParam(name = "page", required = false) Integer pageNumber,
                                              @RequestParam(name = "size", required = false) Integer pageSize,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "code", required = false) String code) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Charge> chargePage = chargeService.findAll(principal, name, code, PageRequest.of(pageNumber, pageSize));

        ChargeDtoPage chargeDtoPage = new ChargeDtoPage(
                chargePage.getContent().stream().map(chargeMapper::map).collect(Collectors.toList()),
                PageRequest.of(chargePage.getPageable().getPageNumber(),
                        chargePage.getPageable().getPageSize()),
                chargePage.getTotalElements()
        );

        return ResponseEntity.ok(chargeDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getChargeById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        return ResponseEntity.ok(chargeMapper.map(chargeService.findById(principal, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteAddressById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        chargeService.delete(principal, id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteAddressByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        chargeService.deleteMany(principal, ids);
        return ResponseEntity.ok(ids);
    }
}
