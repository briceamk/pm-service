package cm.gelodia.pm.organization.api;

import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.commons.service.FileStorageService;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import cm.gelodia.pm.organization.constant.OrganizationConstantTypes;
import cm.gelodia.pm.organization.dto.AddressDto;
import cm.gelodia.pm.organization.dto.AddressDtoPage;
import cm.gelodia.pm.organization.mapper.AddressMapper;
import cm.gelodia.pm.organization.model.Address;
import cm.gelodia.pm.organization.service.AddressService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organization/addresses")
@Api(value = "Address", tags = "Addresses End Points")
public class AddressAPI {

    private final AddressService addressService;
    private final ValidationErrorService validationErrorService;
    private final AddressMapper addressMapper;
    private final FileStorageService fileStorageService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> createAddress(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody AddressDto addressDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        addressDto = addressMapper.map(addressService.create(principal, addressMapper.map(addressDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/organization/addresses/{id}")
                .buildAndExpand(addressDto.getId()).toUri();
        return ResponseEntity.created(uri).body(addressDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> updateAddress(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody AddressDto addressDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return ResponseEntity.ok(addressMapper.map(addressService.update(principal, addressMapper.map(addressDto))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getCompanyByPage(@CurrentPrincipal UserPrincipal principal,
                                              @RequestParam(name = "page", required = false) Integer pageNumber,
                                              @RequestParam(name = "size", required = false) Integer pageSize,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "vat", required = false) String vat,
                                              @RequestParam(name = "trn", required = false) String trn,
                                              @RequestParam(name = "firstName", required = false) String firstName,
                                              @RequestParam(name = "firstName", required = false) String lastName,
                                              @RequestParam(name = "email", required = false) String email,
                                              @RequestParam(name = "phone", required = false) String phone,
                                              @RequestParam(name = "mobile", required = false) String mobile,
                                              @RequestParam(name = "city", required = false) String city,
                                              @RequestParam(name = "fax", required = false) String fax,
                                              @RequestParam(name = "zip", required = false) String zip,
                                              @RequestParam(name = "street", required = false) String street,
                                              @RequestParam(name = "country", required = false) String country) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Address> addressPage = addressService.findAll(principal, name, vat, trn, firstName,
                lastName, email, phone, mobile, city, country, fax, zip, street, PageRequest.of(pageNumber, pageSize));

        AddressDtoPage addressDtoPage = new AddressDtoPage(
                addressPage.getContent().stream().map(addressMapper::map).collect(Collectors.toList()),
                PageRequest.of(addressPage.getPageable().getPageNumber(),
                        addressPage.getPageable().getPageSize()),
                addressPage.getTotalElements()
        );

        return ResponseEntity.ok(addressDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getAddressById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        return ResponseEntity.ok(addressMapper.map(addressService.findById(principal, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteAddressById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        addressService.delete(principal, id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteAddressByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        addressService.deleteMany(principal, ids);
        return ResponseEntity.ok(ids);
    }

    @PutMapping("/upload/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> storeAddressImage(
            @CurrentPrincipal UserPrincipal principal, @PathVariable String id, @RequestParam(name = "field") String field,
            @RequestParam(name = "image") MultipartFile image) {
        AddressDto addressDto = addressMapper.map(addressService.findById(principal, id));
        String imageName = field.equals(OrganizationConstantTypes.FOOTER_FIELD)?
                addressDto.getImageFooterName() != null? addressDto.getImageFooterName() : ""
                :addressDto.getImageHeaderName() != null? addressDto.getImageHeaderName(): "";
        ImageResource imageResource= fileStorageService.fileSystemStoreImage(principal, ImageResource.builder().id(id).imageName(imageName).build(), image);

        return ResponseEntity.ok(addressMapper.map(addressService.update(principal, addressService.storeImage(principal, id, imageResource, field))));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/download/resource/{id}")
    public ResponseEntity<?> getAddressImageAsResource(@CurrentPrincipal UserPrincipal principal, @PathVariable String id, @RequestParam(name = "field") String field) {
        AddressDto addressDto = addressMapper.map(addressService.findById(principal, id));
        String imageName = field.equals(OrganizationConstantTypes.FOOTER_FIELD)?
                addressDto.getImageFooterName() != null? addressDto.getImageFooterName() : ""
                :addressDto.getImageHeaderName() != null? addressDto.getImageHeaderName(): "";
        String imageType = field.equals(OrganizationConstantTypes.FOOTER_FIELD)?
                addressDto.getImageFooterType() != null? addressDto.getImageFooterType() : ""
                :addressDto.getImageHeaderType() != null? addressDto.getImageHeaderType(): "";
        Resource resource = fileStorageService.fileSystemGetImageAsResource(principal, ImageResource.builder().imageName(imageName).build());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName.replace(id + "_", "") + "\"")
                .body(resource);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/download/string/{id}")
    public ResponseEntity<?> getAddressImageAsString(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        AddressDto addressDto = addressMapper.map(addressService.findById(principal, id));
        String imageHeaderName = addressDto.getImageHeaderName() == null? "": addressDto.getImageHeaderName();
        String imageFooterName = addressDto.getImageFooterName() == null? "": addressDto.getImageFooterName();
        List<String> imageHeader = fileStorageService.fileSystemGetImageAsString(principal, ImageResource.builder().imageName(imageHeaderName).build());
        List<String> imageFooter = fileStorageService.fileSystemGetImageAsString(principal, ImageResource.builder().imageName(imageFooterName).build());
        Map<String, Object> imageMap = new LinkedHashMap<>();
        imageMap.put("imageHeader", imageHeader);
        imageMap.put("imageFooter", imageFooter);
        return ResponseEntity.ok().body(imageMap);
    }

}
