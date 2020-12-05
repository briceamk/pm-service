package cm.gelodia.pm.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PermissionDtoPage extends PageImpl<PermissionDto> {

    static final long serialVersionUID = -7985213763212174888L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PermissionDtoPage(@JsonProperty("content") List<PermissionDto> content,
                             @JsonProperty("number") int number,
                             @JsonProperty("size") int size,
                             @JsonProperty("totalElements") Long totalElements,
                             @JsonProperty("pageable") JsonNode pageable,
                             @JsonProperty("last") boolean last,
                             @JsonProperty("totalPages") int totalPages,
                             @JsonProperty("sort") JsonNode sort,
                             @JsonProperty("first") boolean first,
                             @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public PermissionDtoPage(List<PermissionDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PermissionDtoPage(List<PermissionDto> content) {
        super(content);
    }
}
