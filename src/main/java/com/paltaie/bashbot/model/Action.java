package com.paltaie.bashbot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Action {
    private String name;
    private String text;
    private String type;
    private String value;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String style;
}
