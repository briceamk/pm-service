package cm.gelodia.pm;

import cm.gelodia.pm.commons.configuration.FileStorageConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class PmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmServiceApplication.class, args);
    }

}
