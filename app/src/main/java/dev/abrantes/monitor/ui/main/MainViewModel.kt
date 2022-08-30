package dev.abrantes.monitor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.abrantes.monitor.infrastructure.RegisterUrl
import dev.abrantes.monitor.infrastructure.RegisterUrlDao
import dev.abrantes.monitor.infrastructure.Response
import dev.abrantes.monitor.infrastructure.ResponseDao
import dev.abrantes.monitor.infrastructure.TIME
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val responseDao: ResponseDao, private val registerUrlDao: RegisterUrlDao) : ViewModel() {
    fun getAllResponse(): Flow<List<Response>> = responseDao.getAll()

    fun getAllRegisterUrl(): Flow<List<RegisterUrl>> = registerUrlDao.getAll()

    suspend fun insertNewRegisterUrl(uri: String, repeat: TIME) {
        registerUrlDao.addRegisterUrl(RegisterUrl(uri=uri, repeat = repeat))
    }

    suspend fun deleteRegisterUrl(registerUrl: RegisterUrl){
        registerUrlDao.deleteRegisterUrl(registerUrl)
    }

    suspend fun insertNewRequest(uri: String, dat: String, status: Int, requestTime: Int) {
        responseDao.addResponse(Response(uri = uri, dat = dat, status = status, requestTime = requestTime))
    }

}

class MainViewModelFactory(private val responseDao: ResponseDao, private val registerUrlDao: RegisterUrlDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(responseDao, registerUrlDao) as T
        }
        val name = modelClass.name
        throw IllegalArgumentException("Unkonw ViewModel $name")
    }
}