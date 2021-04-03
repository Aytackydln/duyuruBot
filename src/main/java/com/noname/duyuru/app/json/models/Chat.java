package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @deprecated move this to jpa model
 **/
@Deprecated
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class Chat {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
}
