package cm.gelodia.pm.notification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class MailDtoPage extends PageImpl<MailDto> implements Serializable {

    static final long serialVersionUID = 6576977371344260400L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MailDtoPage(@JsonProperty("content") List<MailDto> content,
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

    public MailDtoPage(List<MailDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public MailDtoPage(List<MailDto> content) {
        super(content);
    }
}