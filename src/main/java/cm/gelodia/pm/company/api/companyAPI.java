package cm.gelodia.pm.company.api;

import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.FileStorageService;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.company.dto.CompanyDto;
import cm.gelodia.pm.company.dto.CompanyDtoPage;
import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.company.model.Company;
import cm.gelodia.pm.company.service.CompanyService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/company/companies")
@Api(value = "Company", tags = "Company End Points")
public class companyAPI {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final FileStorageService fileStorageService;
    private final ValidationErrorService validationErrorService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody CompanyDto companyDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;

        companyDto = companyMapper.map(companyService.create(principal, companyMapper.map(companyDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/companies/{id}")
                .buildAndExpand(companyDto.getId()).toUri();
        return ResponseEntity.created(uri).body(companyDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> update(@CurrentPrincipal UserPrincipal principal,
                                    @Valid @RequestBody CompanyDto companyDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return new ResponseEntity<>(companyMapper.map(companyService.update(principal, companyMapper.map(companyDto))), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER' )")
    public ResponseEntity<?> findById(@CurrentPrincipal UserPrincipal principal,
                                      @PathVariable String id) {
        return new ResponseEntity<>(companyMapper.map(companyService.findById(principal, id)), HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> findByCode(@PathVariable String code) {
        return new ResponseEntity<>(companyMapper.map(companyService.findByCode(code)), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER' )")
    public ResponseEntity<?> findAll(@CurrentPrincipal UserPrincipal principal,
                                     @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                     @RequestParam(value = "code", required = false) String code,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                     @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
                                     @RequestParam(value = "email", required = false) String email,
                                     @RequestParam(value = "vat", required = false) String vat,
                                     @RequestParam(value = "trn", required = false) String trn,
                                     @RequestParam(value = "street", required = false) String street,
                                     @RequestParam(value = "city", required = false) String city){


        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Company> companyPage = companyService.findAll(principal, code,
                name, email, phoneNumber, mobileNumber, vat, trn, street,
                city, PageRequest.of(pageNumber, pageSize));

        CompanyDtoPage companyDtoPage = new CompanyDtoPage(
                companyPage.getContent().stream().map(companyMapper::map).collect(Collectors.toList()),
                PageRequest.of(companyPage.getPageable().getPageNumber(),
                        companyPage.getPageable().getPageSize()),
                companyPage.getTotalElements()
        );

        return new ResponseEntity<>( companyDtoPage, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteById(@CurrentPrincipal UserPrincipal principal,
                                        @PathVariable String id) {
        companyService.deleteById(principal, id);
        return new ResponseEntity<>(new ResponseApi(true, "Company deleted successfully!"), HttpStatus.OK);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteByIds(@CurrentPrincipal UserPrincipal principal,
                                        @PathVariable List<String> ids) {
        companyService.deleteByIds(principal, ids);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @PutMapping("/upload/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> uploadLogo(@CurrentPrincipal UserPrincipal principal,
                                              @PathVariable String id, @RequestParam(name = "image") MultipartFile image) {
        ImageResource imageResource =  fileStorageService.dbStoreImage(principal, id, image);
        Company company = companyService.storeLogo(principal, id, imageResource);
        return ResponseEntity.ok(companyService.update(principal, company));
    }


    @GetMapping("/download/resource/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> downloadResource(@CurrentPrincipal UserPrincipal principal,
                                            @PathVariable String id) {
        Company company = companyService.findById(principal, id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(company.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + company.getImageName().replace(id + "_", "") + "\"")
                .body(new ByteArrayResource(company.getImage()));
    }

    @GetMapping("/download/string/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> downloadString(@CurrentPrincipal UserPrincipal principal,
                                          @PathVariable String id) {
        Company company = companyService.findById(principal, id);
        List<String> images = fileStorageService.dbGetImageAsString(
                principal,
                ImageResource.builder()
                        .imageName(company.getImageName())
                        .image(company.getImage())
                        .imageType(company.getImageType())
                        .build()
        );
        return ResponseEntity.ok().body(images);
    }
}
