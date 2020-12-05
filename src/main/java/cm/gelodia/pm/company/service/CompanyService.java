package cm.gelodia.pm.company.service;


import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.company.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CompanyService {
    Company create(UserPrincipal principal, Company company);

    Company update(UserPrincipal principal, Company company);

    Company findById(UserPrincipal principal, String id);

    Company findByCode(String code);

    Page<Company> findAll(UserPrincipal principal, String code, String name,
                          String email, String phoneNumber, String mobileNumber,
                          String vat, String trn, String street, String city, PageRequest pageRequest);

    void deleteById(UserPrincipal principal, String id);

    void deleteByIds(UserPrincipal principal, List<String> ids);


    Company storeLogo(UserPrincipal principal, String id, ImageResource imageResource);

    Boolean containSwearWords(String comment);

}
