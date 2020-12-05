package cm.gelodia.pm.catalog.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.model.Product;
import cm.gelodia.pm.commons.payload.ImageResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {
    Product create(UserPrincipal principal, Product product);

    Product update(UserPrincipal principal, Product product);

    Product findById(UserPrincipal principal, String id);

    Page<Product> findAll(UserPrincipal principal, String reference, String name, String description, PageRequest pageRequest);
    void delete(UserPrincipal principal, String id);
    void deleteMany(UserPrincipal principal, List<String> ids);

    Product storeImage(UserPrincipal principal, String id, ImageResource imageResource);
}
