package karma.convertor.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.multidex.MultiDexApplication
import karma.convertor.BuildConfig
import karma.convertor.R
import karma.convertor.utils.Constants
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import timber.log.Timber

class BaseApplication : MultiDexApplication() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        //Creating the notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
        }

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        FirebaseCrashlytics.getInstance().sendUnsentReports()

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettingsBuilder = FirebaseRemoteConfigSettings.Builder()

        if (!BuildConfig.DEBUG) {
            configSettingsBuilder.minimumFetchIntervalInSeconds = (3 * 3600).toLong() //3 hours
        } else {
            configSettingsBuilder.minimumFetchIntervalInSeconds = 0
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettingsBuilder.build())
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    @RequiresApi(26)
    private fun createNotificationChannels() {
        val name = getString(R.string.push_notification_channel_title)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel =
            NotificationChannel(Constants.PUSH_NOTIFICATION_CHANNEL_ID, name, importance)

        mChannel.setShowBadge(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        private var instance: BaseApplication? = null

        fun getApplicationContext(): Context {
            return instance?.applicationContext!!
        }
    }
}