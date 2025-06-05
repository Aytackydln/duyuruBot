package aytackydln.chattools.telegram.dto.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class KeyboardItem(
    var text: String,
    var callbackData: String? = null,
)
