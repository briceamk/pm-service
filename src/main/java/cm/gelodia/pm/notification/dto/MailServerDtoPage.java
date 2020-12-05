package cm.gelodia.pm.notification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class MailServerDtoPage extends PageImpl<MailServerDto> implements Serializable {

    static final long serialVersionUID = -5976722484735169565L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MailServerDtoPage(@JsonProperty("content") List<MailServerDto> content,
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

    public MailServerDtoPage(List<MailServerDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MailServerDtoPage(List<MailServerDto> content) {
        super(content);
    }
}