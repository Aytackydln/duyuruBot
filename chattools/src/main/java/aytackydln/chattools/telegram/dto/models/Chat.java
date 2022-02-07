package aytackydln.chattools.telegram.dto.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 * @deprecated move this to jpa model
 **/
@Deprecated
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class Chat {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
}
