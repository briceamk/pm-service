package cm.gelodia.pm.company.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class CompanyDtoPage extends PageImpl<CompanyDto> implements Serializable {

    static final long serialVersionUID = 8755280279881490343L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CompanyDtoPage(@JsonProperty("content") List<CompanyDto> content,
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

    public CompanyDtoPage(List<CompanyDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CompanyDtoPage(List<CompanyDto> content) {
        super(content);
    }
}
