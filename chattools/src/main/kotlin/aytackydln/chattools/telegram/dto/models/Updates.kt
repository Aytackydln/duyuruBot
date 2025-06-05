package aytackydln.chattools.telegram.dto.models

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Updates(
    val result: List<Update>,
    val ok: Boolean = false,
) : Iterable<Update> {

    override fun iterator(): Iterator<Update> {
        return result.iterator()
    }
}
