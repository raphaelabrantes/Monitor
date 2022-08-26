package dev.abrantes.monitor.infrastructure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.Date



@Entity
data class Response(
    @PrimaryKey val uri: String,
    @NotNull val date: Date,
    @NotNull val status: Int,
    @NotNull val requestTime: Long
)
