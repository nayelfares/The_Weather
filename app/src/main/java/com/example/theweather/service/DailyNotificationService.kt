package com.example.theweather.service


import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.theweather.MainActivity
import com.example.theweather.R
import com.example.theweather.WeatherViewModel
import com.example.theweather.base.DataState
import com.example.theweather.intent.WeatherIntent
import com.example.theweather.model.CurrentWeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class DailyNotificationService : LifecycleService() {
    var location :String?=null
    @Inject
    lateinit var weatherViewModel: WeatherViewModel

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        subscribeObservers()
        location = intent?.getStringExtra("location")
        if (location!=null){
            lifecycleScope.launch {
                weatherViewModel.userIntent.send(WeatherIntent.GetCurrentWeather(location!!))
            }
        }
    }

    private fun showNotification(temperature:String) {
        val soundUri: Uri = RingtoneManager
            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification: Notification = Notification.Builder(this)
            .setContentTitle("Today Temperature")
            .setContentText("$temperature (f)")
            .setContentIntent(
                PendingIntent.getActivity(
                    this, 0, Intent(
                        this,
                        MainActivity::class.java
                    ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setSound(soundUri).setSmallIcon(R.drawable.ic_launcher_background)
            .build()
        NotificationManagerCompat.from(this).notify(0, notification)
    }

    private fun subscribeObservers() {
        weatherViewModel.currentWeatherDataState.observe(
            this, {
                when (it) {
                    is DataState.Success<CurrentWeatherResponse> -> {
                        showNotification(it.data.current.temp_c.toString())
                    }
                    is DataState.Error -> {}
                    else -> {}
                }
            })
    }

}


