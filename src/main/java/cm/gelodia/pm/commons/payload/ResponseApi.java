package cm.gelodia.pm.commons.payload;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseApi {
    private Boolean success;
    private String message;
}
