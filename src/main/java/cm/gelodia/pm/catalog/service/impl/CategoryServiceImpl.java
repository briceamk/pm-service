package cm.gelodia.pm.catalog.service.impl;


import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.model.Category;
import cm.gelodia.pm.catalog.repository.CategoryRepository;
import cm.gelodia.pm.catalog.service.CategoryService;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Transactional
@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public Category create(UserPrincipal principal, Category category) {

        //We set company
       if(principal.getCompany() == null) {
           log.error("Connected user has no company. contact your administrator");
           throw new BadRequestException("Connected user has no company. contact your administrator");
       }
        //We check reference
        if(categoryRepository.existsByName(category.getName())) {
            log.error("Category with name {} already exist!", category.getName());
            throw new ConflictException(String.format("Category with name %s already exist!",category.getName()));
        }
        category.setCompany(principal.getCompany());
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UserPrincipal principal, Category category) {
        // TODO check unique on update
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(UserPrincipal principal, String id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> {
                    log.error("category with id {} not found!", id);
                    throw new ResourceNotFoundException(String.format("category with id %s not found!", id));
                }
        );
    }

    @Override
    public Page<Category> findAll(UserPrincipal principal, String name, PageRequest pageRequest) {
        Page<Category> categoryPage;

        if(!StringUtils.isEmpty(name))
            categoryPage = categoryRepository.findByNameContainsIgnoreCase(name, pageRequest);
        else
            categoryPage = categoryRepository.findAll(pageRequest);

        return categoryPage;
    }

    @Override
    public void delete(UserPrincipal principal, String id) {
        categoryRepository.delete(findById(principal, id));
    }

    @Override
    public void deleteMany(UserPrincipal principal, List<String> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if(categories.size() != ids.size()) {
            log.error("some categories does not exits!");
            throw new ResourceNotFoundException("some categories does not exits!");
        }
        categoryRepository.deleteInBatch(categories);
    }
}
