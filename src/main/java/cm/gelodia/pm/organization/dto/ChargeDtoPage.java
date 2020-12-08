package cm.gelodia.pm.organization.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class ChargeDtoPage extends PageImpl<ChargeDto> implements Serializable {

    static final long serialVersionUID = 8646240545146824990L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ChargeDtoPage(@JsonProperty("content") List<ChargeDto> content,
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

    public ChargeDtoPage(List<ChargeDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
            }

    public ChargeDtoPage(List<ChargeDto> content) {
            super(content);
            }
}