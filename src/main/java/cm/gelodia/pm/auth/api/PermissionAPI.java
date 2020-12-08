package cm.gelodia.pm.auth.api;


import cm.gelodia.pm.auth.dto.PermissionDtoPage;
import cm.gelodia.pm.auth.dto.PermissionDto;
import cm.gelodia.pm.auth.mapper.PermissionMapper;
import cm.gelodia.pm.auth.model.PermissionCode;
import cm.gelodia.pm.auth.model.Permission;
import cm.gelodia.pm.auth.service.PermissionService;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.service.ValidationErrorService;
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
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/permissions")
@Api(value = "Permission", tags = "Permission End Point")
public class PermissionAPI {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;
    private final ValidationErrorService validationErrorService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create( @Valid @RequestBody PermissionDto permissionDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        permissionDto = permissionMapper.map(permissionService.create( permissionMapper.map(permissionDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/auth/permissions/{id}")
                .buildAndExpand(permissionDto.getId()).toUri();
        return ResponseEntity.created(uri).body(permissionDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> updatePermission( @Valid @RequestBody PermissionDto permissionDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;

        return ResponseEntity.ok().body(permissionMapper.map(permissionService.update( permissionMapper.map(permissionDto))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public @ResponseBody ResponseEntity<?> findAll( 
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name) {
        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }
        Page<Permission> permissionPage = permissionService.findAll(code!= null && !code.isEmpty()? PermissionCode.valueOf(code): null, name, PageRequest.of(pageNumber, pageSize));
        PermissionDtoPage permissionDtoPage = new PermissionDtoPage(
                permissionPage.getContent().stream().map(permissionMapper::map).collect(Collectors.toList()),
                PageRequest.of(permissionPage.getPageable().getPageNumber(),
                        permissionPage.getPageable().getPageSize()),
                permissionPage.getTotalElements()
        );

        return ResponseEntity.ok(permissionDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> getPermissionById(
            @PathVariable String id) {
        return ResponseEntity.ok(permissionMapper.map(permissionService.findById( id)));
    }


}
