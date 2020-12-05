package cm.gelodia.pm.catalog.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class CategoryDtoPage extends PageImpl<CategoryDto> implements Serializable {

    static final long serialVersionUID = 7547122474424654131L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CategoryDtoPage(@JsonProperty("content") List<CategoryDto> content,
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

    public CategoryDtoPage(List<CategoryDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CategoryDtoPage(List<CategoryDto> content) {
        super(content);
    }
}

