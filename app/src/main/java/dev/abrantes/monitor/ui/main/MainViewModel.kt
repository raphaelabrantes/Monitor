package dev.abrantes.monitor.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.abrantes.monitor.infrastructure.RegisterUrl
import dev.abrantes.monitor.infrastructure.RegisterUrlDao
import dev.abrantes.monitor.infrastructure.Response
import dev.abrantes.monitor.infrastructure.ResponseDao
import dev.abrantes.monitor.infrastructure.TIME
import dev.abrantes.monitor.worker.HttpRequestWorker
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val responseDao: ResponseDao,
    private val registerUrlDao: RegisterUrlDao,
    application: Application
) : ViewModel() {

    private val workerManager = WorkManager.getInstance(application)

    fun getAllResponse(): Flow<List<Response>> = responseDao.getAll()

    fun getAllRegisterUrl(): Flow<List<RegisterUrl>> = registerUrlDao.getAll()

    //FIXME: Change calls to use id instead of uri, need to change the Entity to have 2 primary keys uri and repeation
    fun getAllResponseWithUri(uri: String): Flow<List<Response>> = responseDao.getAllByUri(uri)

    suspend fun insertNewRegisterUrl(uri: String, repeat: TIME) {
        val registerUrl = RegisterUrl(uri = uri, repeat = repeat)
        registerUrlDao.addRegisterUrl(registerUrl)
    }

    suspend fun deleteRegisterUrl(registerUrl: RegisterUrl) {
        registerUrlDao.deleteRegisterUrl(registerUrl)
        cancelWork(registerUrl)
    }

    fun startRequest(uri: String, repeat: TIME) {
        Log.d(javaClass.name,"start request")
        val time = when(repeat) {
            TIME.FIFTEEN_MINUTES-> 15L
            TIME.TWENTY_MINUTES -> 20L
            TIME.THIRTY_MINUTES -> 30L
        }
        val builder = PeriodicWorkRequestBuilder<HttpRequestWorker>(time, TimeUnit.MINUTES)
            .addTag(uri)
            .setInputData(
                workDataOf(
                    "registerUrl" to uri
                )
            )
        workerManager.enqueueUniquePeriodicWork(uri, ExistingPeriodicWorkPolicy.REPLACE, builder.build())
    }

    private fun cancelWork(registerUrl: RegisterUrl) {
        workerManager.cancelAllWorkByTag(registerUrl.uri)
    }

}

class MainViewModelFactory(
    private val responseDao: ResponseDao,
    private val registerUrlDao: RegisterUrlDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(responseDao, registerUrlDao, application) as T
        }
        val name = modelClass.name
        throw IllegalArgumentException("Unkonw ViewModel $name")
    }
}