package com.noname.duyuru.app.component;

import com.noname.duyuru.app.service.UserService;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@Value
public class ServiceContext {
    UserService userService;
}
