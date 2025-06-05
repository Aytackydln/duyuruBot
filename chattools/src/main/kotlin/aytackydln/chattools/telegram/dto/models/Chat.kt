package aytackydln.chattools.telegram.dto.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Data

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Deprecated("move this to jpa model")
data class Chat (
    val id: Long = 0,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
)
