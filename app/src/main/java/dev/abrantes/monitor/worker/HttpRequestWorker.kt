package dev.abrantes.monitor.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.abrantes.monitor.R
import dev.abrantes.monitor.infrastructure.AppDatabase
import dev.abrantes.monitor.infrastructure.Response
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*

class HttpRequestWorker(
    context: Context,
    params: WorkerParameters
) :
    CoroutineWorker(context, params) {
    private val dataFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")

    override suspend fun doWork(): Result {
        val registerUrl = inputData.getString("registerUrl")
        registerUrl?.let {
            Log.d("WORKER", "DEU BOM 1")
            val respondeDao = AppDatabase.getDatabase(applicationContext).responseDao()
            val client = OkHttpClient()
            val request = Request.Builder().url(it).build()
            val current = dataFormat.format(Date())
            val response: okhttp3.Response
            try {
                response = client.newCall(request).execute()
            } catch (exception: Exception) {
                Log.e("WORKER", "FUCKED UP")
                sendNotification("${exception.message}")
                exception.printStackTrace()
                return@let Result.success()
            }
            if (!response.isSuccessful){
                sendNotification(" $it replied with code ${response.code}")
            }
            respondeDao.addResponse(
                Response(
                    uri = registerUrl,
                    dat = current,
                    status = response.code,
                    requestTime = response.receivedResponseAtMillis - response.sentRequestAtMillis
                )
            )
        }
        return Result.success()
    }

    private fun sendNotification(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Monitor"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(name, "Request Failed", importance).apply {
                description = message
                enableVibration(true)
            }
            val notificationManager =
                getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val build = NotificationCompat
                .Builder(applicationContext, name)
                .setContentTitle("Monitor: Something went wrong")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(55555, build.build())
            }
        }
    }
}