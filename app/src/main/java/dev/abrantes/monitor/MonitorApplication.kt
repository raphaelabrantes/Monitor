package dev.abrantes.monitor

import android.app.Application
import dev.abrantes.monitor.infrastructure.AppDatabase

class MonitorApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this)}
}
