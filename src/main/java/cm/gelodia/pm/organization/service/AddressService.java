package cm.gelodia.pm.organization.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.organization.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface AddressService {
    Address create(UserPrincipal principal, Address address);

    Address update(UserPrincipal principal, Address address);

    Page<Address> findAll(UserPrincipal principal, String name, String vat, String trn,
                          String firstName, String lastName, String email, String phone, String mobile, 
                          String city, String country, String fax, String zip, String street, PageRequest pageRequest);

    void delete(UserPrincipal principal, String id);

    Address findById(UserPrincipal principal, String id);

    void deleteMany(UserPrincipal principal, List<String> ids);

    Address storeImage(UserPrincipal principal, String id, ImageResource imageResource, String field);
}
