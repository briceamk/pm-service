package cm.gelodia.pm.organization.repository;

import cm.gelodia.pm.organization.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
    Page<Address> findByStreetContainsIgnoreCase(String street, Pageable pageable);

    Page<Address> findByZipContainsIgnoreCase(String zip, Pageable pageable);

    Page<Address> findByFaxContainsIgnoreCase(String fax, Pageable pageable);

    Page<Address> findByCountryContainsIgnoreCase(String country, Pageable pageable);

    Page<Address> findByCityContainsIgnoreCase(String city, Pageable pageable);

    Page<Address> findByMobileContainsIgnoreCase(String mobile, Pageable pageable);

    Page<Address> findByPhoneContainsIgnoreCase(String phone, Pageable pageable);

    Page<Address> findByEmailContainsIgnoreCase(String email, Pageable pageable);

    Page<Address> findByLastNameContainsIgnoreCase(String lastName, Pageable pageable);

    Page<Address> findByFirstNameContainsIgnoreCase(String firstName, Pageable pageable);

    Page<Address> findByTrnContainsIgnoreCase(String trn, Pageable pageable);

    Page<Address> findByVatContainsIgnoreCase(String vat, Pageable pageable);

    Page<Address> findByNameContainsIgnoreCase(String name, Pageable pageable);
}
