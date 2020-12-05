package cm.gelodia.pm.catalog.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    Category create(UserPrincipal principal, Category category);
    Category update(UserPrincipal principal, Category category);
    Category findById(UserPrincipal principal, String id);
    Page<Category> findAll(UserPrincipal principal, String name, PageRequest pageRequest);
    void delete(UserPrincipal principal, String id);
    void deleteMany(UserPrincipal principal, List<String> ids);
}
