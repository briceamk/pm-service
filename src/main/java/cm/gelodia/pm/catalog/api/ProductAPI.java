package cm.gelodia.pm.catalog.api;


import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.dto.ProductDto;
import cm.gelodia.pm.catalog.dto.ProductDtoPage;
import cm.gelodia.pm.catalog.mapper.ProductMapper;
import cm.gelodia.pm.catalog.model.Product;
import cm.gelodia.pm.catalog.service.ProductService;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.FileStorageService;
import cm.gelodia.pm.commons.service.ValidationErrorService;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog/products")
@Api(value = "Product", tags = "Products End Points")
public class ProductAPI {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ValidationErrorService validationErrorService;
    private final FileStorageService fileStorageService;
    

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> createProduct(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody ProductDto productDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        productDto = productMapper.map(productService.create(principal, productMapper.map(productDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/catalog/products/{id}")
                .buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> updateProduct(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody ProductDto productDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return ResponseEntity.ok(productMapper.map(productService.update(principal, productMapper.map(productDto))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getCompanyByPage(@CurrentPrincipal UserPrincipal principal,
                                              @RequestParam(name = "page", required = false) Integer pageNumber,
                                              @RequestParam(name = "size", required = false) Integer pageSize,
                                              @RequestParam(name = "reference", required = false) String reference,
                                              @RequestParam(name = "name", required = false) String name, 
                                              @RequestParam(name = "description", required = false) String description) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Product> productPage = productService.findAll(principal, reference, name, description, PageRequest.of(pageNumber, pageSize));

        ProductDtoPage productDtoPage = new ProductDtoPage(
                productPage.getContent().stream().map(productMapper::map).collect(Collectors.toList()),
                PageRequest.of(productPage.getPageable().getPageNumber(),
                        productPage.getPageable().getPageSize()),
                productPage.getTotalElements()
        );

        return ResponseEntity.ok(productDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getCompanyById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        return ResponseEntity.ok(productMapper.map(productService.findById(principal, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteProductById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        productService.delete(principal, id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteProductByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        productService.deleteMany(principal, ids);
        return ResponseEntity.ok(ids);
    }

    @PutMapping("/upload/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> storeProductImage(
            @CurrentPrincipal UserPrincipal principal, @PathVariable String id,
            @RequestParam(name = "image") MultipartFile image) {
        ProductDto productDto = productMapper.map(productService.findById(principal, id));
        ImageResource imageResource= fileStorageService.fileSystemStoreImage(principal, ImageResource.builder().id(id).imageName(productDto.getImageName()).build(), image);

        return ResponseEntity.ok(productMapper.map(productService.update(principal, productService.storeImage(principal, id, imageResource))));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/download/resource/{id}")
    public ResponseEntity<?> getProductImageAsResource(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        ProductDto productDto = productMapper.map(productService.findById(principal, id));
        Resource resource = fileStorageService.fileSystemGetImageAsResource(principal, ImageResource.builder().imageName(productDto.getImageName()).build());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(productDto.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + productDto.getImageName().replace(id + "_", "") + "\"")
                .body(resource);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    @GetMapping("/download/string/{id}")
    public ResponseEntity<?> getProductImageAsString(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        ProductDto productDto = productMapper.map(productService.findById(principal, id));
        List<String> images = fileStorageService.fileSystemGetImageAsString(principal, ImageResource.builder().imageName(productDto.getImageName()).build());
        return ResponseEntity.ok().body(images);
    }

}
