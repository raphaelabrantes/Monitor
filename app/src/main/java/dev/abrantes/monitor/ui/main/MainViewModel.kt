package dev.abrantes.monitor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.abrantes.monitor.infrastructure.Response
import dev.abrantes.monitor.infrastructure.ResponseDao
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val responseDao: ResponseDao) : ViewModel() {
    fun getAll() : Flow<List<Response>> = responseDao.getAll()

}

class MainViewModelFactory(private  val responseDao: ResponseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(responseDao) as T
        }
        val name = modelClass.name
        throw IllegalArgumentException("Unkonw ViewModel $name")
    }
}