package cm.gelodia.pm.commons.exception;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorDetails {
    private Integer status;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using =  LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    private String message;
    private String detail;
}
