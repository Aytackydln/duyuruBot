package aytackydln.duyuru.component;

import aytackydln.duyuru.service.ChatUserService;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@Value
public class ServiceContext {
    ChatUserService chatUserService;
}
