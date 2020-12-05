package cm.gelodia.pm.catalog.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.catalog.model.Catalog;
import cm.gelodia.pm.catalog.repository.CatalogRepository;
import cm.gelodia.pm.catalog.service.CatalogService;
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
@Service("catalogService")
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public Catalog create(UserPrincipal principal, Catalog catalog) {

        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }

        //We check reference
        if(catalogRepository.existsByName(catalog.getName())) {
            log.error("Catalog with name {} already exist!", catalog.getName());
            throw new ConflictException(String.format("Catalog with name %s already exist!",catalog.getName()));
        }

        catalog.setCompany(principal.getCompany());
        return catalogRepository.save(catalog);
    }

    @Override
    public Catalog update(UserPrincipal principal, Catalog catalog) {
        // TODO check unique on update
        //We set company
        return catalogRepository.save(catalog);
    }

    @Override
    public Catalog findById(UserPrincipal principal, String id) {
        return catalogRepository.findById(id).orElseThrow(() -> {
            log.error("catalog with id {} not found!", id);
            throw new ResourceNotFoundException(String.format("catalog with id %s not found!", id));
        });
    }

    @Override
    public Page<Catalog> findAll(UserPrincipal principal, String name, String description, PageRequest pageRequest) {
        Page<Catalog> catalogPage;

        if(!StringUtils.isEmpty(name)) {
            catalogPage = catalogRepository.findByNameContainsIgnoreCase(name, pageRequest);
        }

       else if(!StringUtils.isEmpty(description)) {
                catalogPage = catalogRepository.findByDescriptionContainsIgnoreCase(description, pageRequest);
            }
       else {
            catalogPage = catalogRepository.findAll(pageRequest);
        }


        return catalogPage;
    }

    @Override
    public void delete(UserPrincipal principal, String id) {
        catalogRepository.delete(findById(principal, id));
    }

    @Override
    public void deleteMany(UserPrincipal principal, List<String> ids) {
        List<Catalog> categories = catalogRepository.findAllById(ids);
        if(categories.size() != ids.size()) {
            log.error("some catalogs does not exits!");
            throw new ResourceNotFoundException(String.format("some catalogs does not exits!"));
        }
        catalogRepository.deleteInBatch(categories);
    }
}
