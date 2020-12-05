package cm.gelodia.pm.catalog.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.model.Catalog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CatalogService {
    Catalog create(UserPrincipal principal, Catalog catalog);
    Catalog update(UserPrincipal principal, Catalog catalog);
    Catalog findById(UserPrincipal principal, String id);
    Page<Catalog> findAll(UserPrincipal principal, String name, String description, PageRequest pageRequest);
    void delete(UserPrincipal principal, String id);
    void deleteMany(UserPrincipal principal, List<String> ids);
}
