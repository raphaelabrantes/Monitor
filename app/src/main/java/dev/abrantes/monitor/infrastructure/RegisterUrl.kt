package dev.abrantes.monitor.infrastructure

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class RegisterUrl(
    @PrimaryKey(autoGenerate = true) @NotNull val id: Int = 0,
    @NotNull val uri: String,
)
