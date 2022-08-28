package dev.abrantes.monitor.infrastructure

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


enum class TIME {
    ONE_SECOND,
    ONE_MINUTE,
    FIVE_MINUTE,
}
@Entity
data class RegisterUrl(
    @PrimaryKey(autoGenerate = true) @NotNull val id: Int = 0,
    @NotNull var uri: String,
    @NotNull var repeat: TIME,

)
