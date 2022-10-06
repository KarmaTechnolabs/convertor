package karma.convertor.utils

import karma.convertor.base.BaseApplication
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

object RemoteConfigManager {
    /**
     * Load Remote Config values and activate them
     * EventBus is called to refresh the UI based on config change
     */
    @JvmStatic
    fun checkRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            Timber.d("Values activated")
            EventBus.getDefault().post(RemoteConfigChanged())
        }.addOnFailureListener { e ->
            Timber.e(e)
        }
    }


    @JvmStatic
    fun getLatestAppVersion(): String {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        var version = remoteConfig.getString(RemoteConfigKey.LATEST_APP_VERSION)
        if (version.isBlank()) {
            version = BaseApplication.getApplicationContext().let {
                it.packageManager.getPackageInfo(it.packageName, 0).versionName
            }
        }
        return version
    }
}
