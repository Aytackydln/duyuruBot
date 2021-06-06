package com.noname.duyuru.app.json.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.noname.duyuru.app.jpa.models.User;
import lombok.Data;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class ChatMember {
    User user;
    String status;
}
