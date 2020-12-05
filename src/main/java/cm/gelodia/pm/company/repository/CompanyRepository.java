package cm.gelodia.pm.company.repository;

import cm.gelodia.pm.company.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, String>  {
    Boolean existsByCode(String code);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByMobileNumber(String email);

    Boolean existsByVat(String vatNumber);

    Boolean existsByTrn(String tradeRegisterNumber);

    Optional<Company> findByCodeIgnoreCase(String code);

    Page<Company> findByCodeContainsIgnoreCase(String code, Pageable pageable);

    Page<Company> findByNameContainsIgnoreCase(String name, Pageable pageable);

    Page<Company> findByEmailContainsIgnoreCase(String email, Pageable pageable);

    Page<Company> findByPhoneNumberIgnoreCase(String phoneNumber, Pageable pageable);

    Page<Company> findByMobileNumberIgnoreCase(String mobileNumber, Pageable pageable);

    Page<Company> findByVatContainsIgnoreCase(String vat, Pageable pageable);

    Page<Company> findByTrnContainsIgnoreCase(String trn, Pageable pageable);

    Page<Company> findByCityContainsIgnoreCase(String city, Pageable pageable);

    Page<Company> findByStreetContainsIgnoreCase(String street, Pageable pageable);

}
