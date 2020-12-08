package cm.gelodia.pm.organization.service;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.organization.model.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ChargeService {
    Charge create(UserPrincipal principal, Charge charge);

    Charge update(UserPrincipal principal, Charge charge);

    Page<Charge> findAll(UserPrincipal principal, String name, String code, PageRequest pageRequest);

    Charge findById(UserPrincipal principal, String id);

    void delete(UserPrincipal principal, String id);

    void deleteMany(UserPrincipal principal, List<String> ids);
}
