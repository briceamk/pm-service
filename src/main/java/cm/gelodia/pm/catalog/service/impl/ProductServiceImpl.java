package cm.gelodia.pm.catalog.service.impl;


import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.model.Product;
import cm.gelodia.pm.catalog.repository.ProductRepository;
import cm.gelodia.pm.catalog.service.ProductService;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.commons.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Transactional
@Service("productService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Product create(UserPrincipal principal, Product product) {
        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }

        //We check reference
        if(productRepository.existsByName(product.getName())) {
            log.error("Product with name {} already exist!", product.getName());
            throw new ConflictException(String.format("Product with name %s already exist!",product.getName()));
        }
        //We check reference
        if(productRepository.existsByReference(product.getReference())) {
            log.error("Product with reference {} already exist!", product.getReference());
            throw new ConflictException(String.format("Product with reference %s already exist!",product.getReference()));
        }

        product.setCompany(principal.getCompany());
        //We save default image
        product = productRepository.save(product);
        //We set and save default image
        MultipartFile file = fileStorageService.fileSystemDefaultImage(principal);
        ImageResource imageResource = fileStorageService.fileSystemStoreImage(principal, ImageResource.builder().id(product.getId()).build(), file);
        product = storeImage(principal, product.getId(), imageResource);
        return productRepository.save(product);
    }



    @Override
    public Product update(UserPrincipal principal, Product product) {
        // TODO check unique on update
        return productRepository.save(product);
    }

    @Override
    public Product findById(UserPrincipal principal, String id) {
        return  productRepository.findById(id).orElseThrow(() -> {
            log.error("product with id {} not found!", id);
            throw new ResourceNotFoundException(String.format("product with id %s not found!", id));
        });
    }

    @Override
    public Page<Product> findAll(UserPrincipal principal, String reference, String name, String description, PageRequest pageRequest) {

        Page<Product> productPage;

        if(!StringUtils.isEmpty(reference))
            productPage = productRepository.findByReferenceContainsIgnoreCase(reference, pageRequest);
        else if(!StringUtils.isEmpty(name))
            productPage = productRepository.findByNameContainsIgnoreCase(name, pageRequest);
        else if(!StringUtils.isEmpty(description))
            productPage = productRepository.findByDescriptionContainsIgnoreCase(description, pageRequest);
        else
            productPage = productRepository.findAll(pageRequest);

        return productPage;
    }

    @Override
    public void delete(UserPrincipal principal, String id) {
        productRepository.delete(findById(principal, id));
    }

    @Override
    public void deleteMany(UserPrincipal principal, List<String> ids) {
        List<Product> categories = productRepository.findAllById(ids);
        if(categories.size() != ids.size()) {
            log.error("some categories does not exits!");
            throw new ResourceNotFoundException("some categories does not exits!");
        }
        productRepository.deleteInBatch(categories);
    }

    @Override
    public Product storeImage(UserPrincipal principal, String id, ImageResource imageResource) {
        Product product = findById(principal, id);
        if(imageResource != null) {
            if(imageResource.getImageName() != null) {
                product.setImageName(imageResource.getImageName());
            }
            if(imageResource.getImageType() != null) {
                product.setImageType(imageResource.getImageType());
            }
        }
        return product;
    }

}
