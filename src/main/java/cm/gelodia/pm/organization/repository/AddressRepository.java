package cm.gelodia.pm.organization.repository;

import cm.gelodia.pm.organization.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
    Page<Address> findByStreetContainsIgnoreCase(String street, PageRequest pageRequest);

    Page<Address> findByZipContainsIgnoreCase(String zip, PageRequest pageRequest);

    Page<Address> findByFaxContainsIgnoreCase(String fax, PageRequest pageRequest);

    Page<Address> findByCountryContainsIgnoreCase(String country, PageRequest pageRequest);

    Page<Address> findByCityContainsIgnoreCase(String city, PageRequest pageRequest);

    Page<Address> findByMobileContainsIgnoreCase(String mobile, PageRequest pageRequest);

    Page<Address> findByPhoneContainsIgnoreCase(String phone, PageRequest pageRequest);

    Page<Address> findByEmailContainsIgnoreCase(String email, PageRequest pageRequest);

    Page<Address> findByLastNameContainsIgnoreCase(String lastName, PageRequest pageRequest);

    Page<Address> findByFirstNameContainsIgnoreCase(String firstName, PageRequest pageRequest);

    Page<Address> findByTrnContainsIgnoreCase(String trn, PageRequest pageRequest);

    Page<Address> findByVatContainsIgnoreCase(String vat, PageRequest pageRequest);

    Page<Address> findByNameContainsIgnoreCase(String name, PageRequest pageRequest);
}
