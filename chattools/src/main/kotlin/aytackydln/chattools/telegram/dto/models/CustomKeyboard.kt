package aytackydln.chattools.telegram.dto.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Data
import lombok.RequiredArgsConstructor

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class CustomKeyboard : Keyboard {
    @get:JsonProperty
    val keyboard: MutableList<MutableList<KeyboardItem>> = ArrayList()

    @get:JsonProperty
    val isResizeKeyboard: Boolean
        get() = true

    @get:JsonProperty
    val isOneTimeKeyboard: Boolean
        get() = true

    fun addRow(): MutableList<KeyboardItem> {
        val newList: MutableList<KeyboardItem> = ArrayList()
        keyboard.add(newList)
        return newList
    }
}
