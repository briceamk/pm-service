package cm.gelodia.pm.notification.mapper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class DateTimeMapper {
    public LocalDateTime map(Timestamp ts) {
        if(ts != null)
            return ts.toLocalDateTime();
        else
            return null;
    }

    public Timestamp map(LocalDateTime  ldt) {
        if(ldt != null)
            return Timestamp.valueOf(ldt);
        else
            return null;
    }
}

