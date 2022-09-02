package dev.abrantes.monitor.infrastructure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity
data class Response(
    @PrimaryKey(autoGenerate = true) @NotNull val id: Int = 0,
    @NotNull val uri: String,
    @NotNull val dat: String,
    @NotNull val status: Int,
    @ColumnInfo(name = "request_time") @NotNull val requestTime: Long
)
