package cm.gelodia.pm.catalog.api;


import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.dto.CatalogDto;
import cm.gelodia.pm.catalog.dto.CatalogDtoPage;
import cm.gelodia.pm.catalog.mapper.CatalogMapper;
import cm.gelodia.pm.catalog.model.Catalog;
import cm.gelodia.pm.catalog.service.CatalogService;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.service.ValidationErrorService;
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
@RequestMapping("/api/v1/catalog/catalogs")
@Api(value = "Catalog", tags = "Catalogs End Points")
public class CatalogAPI {

    private final CatalogService catalogService;
    private final CatalogMapper catalogMapper;
    private final ValidationErrorService validationErrorService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<?> createCatalog(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody CatalogDto catalogDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        catalogDto = catalogMapper.map(catalogService.create(principal, catalogMapper.map(catalogDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/catalog/catalogs/{id}")
                .buildAndExpand(catalogDto.getId()).toUri();
        return ResponseEntity.created(uri).body(catalogDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<?> updateCatalog(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody CatalogDto catalogDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return  ResponseEntity.ok(catalogMapper.map(catalogService.update(principal, catalogMapper.map(catalogDto))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> getCompanyByPage(@CurrentPrincipal UserPrincipal principal,
                                              @RequestParam(name = "page", required = false) Integer pageNumber,
                                              @RequestParam(name = "size", required = false) Integer pageSize,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "description", required = false) String description ) {

        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Catalog> catalogPage = catalogService.findAll(principal, name, description, PageRequest.of(pageNumber, pageSize));

        CatalogDtoPage catalogDtoPage = new CatalogDtoPage(
                catalogPage.getContent().stream().map(catalogMapper::map).collect(Collectors.toList()),
                PageRequest.of(catalogPage.getPageable().getPageNumber(),
                        catalogPage.getPageable().getPageSize()),
                catalogPage.getTotalElements()
        );

        return ResponseEntity.ok(catalogDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> getCompanyById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        return ResponseEntity.ok(catalogMapper.map(catalogService.findById(principal, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<?> deleteCatalogById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        catalogService.delete(principal, id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseEntity<?> deleteCatalogByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        catalogService.deleteMany(principal, ids);
        return ResponseEntity.ok(ids);
    }

}
