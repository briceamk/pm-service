package cm.gelodia.pm.catalog.api;


import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.dto.CategoryDto;
import cm.gelodia.pm.catalog.dto.CategoryDtoPage;
import cm.gelodia.pm.catalog.mapper.CategoryMapper;
import cm.gelodia.pm.catalog.model.Category;
import cm.gelodia.pm.catalog.service.CategoryService;
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
@RequestMapping("/api/v1/catalog/categories")
@Api(value = "Product Category", tags = "Products Categories End Points")
public class CategoryAPI {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ValidationErrorService validationErrorService;
    

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> createCategory(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody CategoryDto categoryDto, BindingResult result) {

        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        categoryDto =categoryMapper.map( categoryService.create(principal, categoryMapper.map(categoryDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/catalog/categories/{id}")
                .buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> updateCategory(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody CategoryDto categoryDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        return ResponseEntity.ok(categoryMapper.map(categoryService.update(principal, categoryMapper.map(categoryDto))) );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getCompanyByPage(@CurrentPrincipal UserPrincipal principal,
                                              @RequestParam(name = "page", required = false) Integer pageNumber,
                                              @RequestParam(name = "size", required = false) Integer pageSize,
                                              @RequestParam(name = "name", required = false) String name) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }

        Page<Category> categoryPage = categoryService.findAll(principal, name, PageRequest.of(pageNumber, pageSize));

        CategoryDtoPage categoryDtoPage = new CategoryDtoPage(
                categoryPage.getContent().stream().map(categoryMapper::map).collect(Collectors.toList()),
                PageRequest.of(categoryPage.getPageable().getPageNumber(),
                        categoryPage.getPageable().getPageSize()),
                categoryPage.getTotalElements()
        );

        return ResponseEntity.ok(categoryDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_USER')")
    public ResponseEntity<?> getCompanyById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        return ResponseEntity.ok(categoryMapper.map(categoryService.findById(principal, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteCategoryById(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        categoryService.delete(principal, id);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/many/{ids}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> deleteCategoryByIds(@CurrentPrincipal UserPrincipal principal, @PathVariable List<String> ids) {
        categoryService.deleteMany(principal, ids);
        return ResponseEntity.ok(ids);
    }

}
