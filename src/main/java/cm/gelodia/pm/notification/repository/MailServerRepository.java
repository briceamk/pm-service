package cm.gelodia.pm.notification.repository;

import cm.gelodia.pm.notification.model.MailServer;
import cm.gelodia.pm.notification.model.MailServerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailServerRepository extends JpaRepository<MailServer, String> {

    Integer countByTypeAndDefaultServer(MailServerType type, Boolean defaultServer);

    Optional<MailServer> findByTypeAndDefaultServer(MailServerType type, Boolean defaultServer);

    Boolean existsByHostnameAndType(String hostname, MailServerType type);

    Page<MailServer> findByHostnameContains(String hostname, Pageable pageable);
}
