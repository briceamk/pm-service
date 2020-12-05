package cm.gelodia.pm.auth.repository;


import cm.gelodia.pm.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User>  findByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM User u WHERE (u.username = :username OR u.email = :email) AND u.company.code = :companyCode ")
    Optional<User> findByUsernameOrEmailAndCompanyCode(@Param("username") String username,@Param("email")  String email,@Param("companyCode")  String companyCode);

    @Query("SELECT new java.lang.Boolean(COUNT(*) > 0) FROM User u WHERE u.username = :username AND u.company.code = :companyCode")
    Boolean checkByUsernameAndCompanyCode(String username, String companyCode);

    @Query("SELECT new java.lang.Boolean(COUNT(*) > 0) FROM User u WHERE  u.email = :email AND u.company.code = :companyCode")
    Boolean checkByEmailAndCompanyCode(String email, String companyCode);

    Boolean existsByEmail(String email);

    Boolean existsByMobile(String email);

    Page<User> findByFirstNameContains(String firstName, Pageable pageable);

    Page<User> findByLastNameContains(String lastName, Pageable pageable);

    Page<User> findByUsernameContains(String username, Pageable pageable);

    Page<User> findByEmailContains(String email, Pageable pageable);

    Page<User> findByMobileContains(String mobile, Pageable pageable);
}
