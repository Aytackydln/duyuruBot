package aytackydln.chattools.telegram.dto.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class InlineKeyboard : Keyboard {
    @get:JsonProperty("inline_keyboard")
    val content: MutableList<MutableList<KeyboardItem>> = ArrayList()

    @get:JsonProperty
    val isOneTimeKeyboard: Boolean
        get() = true

    fun addRow(): MutableList<KeyboardItem> {
        val newList: MutableList<KeyboardItem> = ArrayList()
        content.add(newList)
        return newList
    }
}
