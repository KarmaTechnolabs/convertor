package karma.convertor.utils

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import karma.convertor.base.BaseActivity
import karma.convertor.custom.getPreferenceValue
import karma.convertor.custom.savePreferenceValue
import karma.convertor.custom.showToast
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlin.math.max

const val MAJOR_UPDATE_REQUEST_CODE = 2019
const val OPTIONAL_UPDATE_REQUEST_CODE = 2020

class InAppUpdateManager(val activity: BaseActivity, val listener: InAppUpdateListener) {

    private val appVersionFormat = "[0-9]+\\.[0-9]+\\.[0-9]+"

    private val appUpdateManager = AppUpdateManagerFactory.create(activity)

    private val currentAppVersion = activity.packageManager.getPackageInfo(
            activity.packageName, 0).versionName
    private var newAppVersion: String? = null
    private var updateType: UpdateType? = null

    private val installUpdateListener: InstallStateUpdatedListener = InstallStateUpdatedListener {
        when {
            it.installStatus() == InstallStatus.PENDING -> {
                listener.onStartUpdate()
            }
            it.installStatus() == InstallStatus.DOWNLOADING -> {
                val percentage = (it.bytesDownloaded() * 100) / it.totalBytesToDownload()
                listener.onProgressUpdate("${percentage.toInt()}%")
            }
            it.installStatus() == InstallStatus.DOWNLOADED -> {
                listener.onFinishUpdate()
                unregisterListener()
                if (updateType == UpdateType.MAJOR) {
                    installUpdate()
                }
            }
        }
    }

    private fun unregisterListener() {
        appUpdateManager.unregisterListener(installUpdateListener)
    }

    fun checkForUpdate() {

        //You can use Remote Config or Your own API to get the latest version
        newAppVersion = RemoteConfigManager.getLatestAppVersion()
        newAppVersion?.let {
            if (currentAppVersion != null) {
                updateType = getUpdateType(currentAppVersion, it)
                updateType?.let { checkUpdateInGooglePlay() }
            }
        }

       /* dashboardApiService.getLatestAppVersion(currentAppVersion,
                object : NetworkResponseCreator.NetworkResponseListener<GetAppVersionResponse>() {
                    override fun onSuccess(modelClass: GetAppVersionResponse?) {
                        newAppVersion = modelClass?.appVersion
                        newAppVersion?.let { newAppVersion ->
                            if (currentAppVersion != null) {
                                updateType = getUpdateType(currentAppVersion, newAppVersion)
                                if (updateType != null) {
                                    checkUpdateInGooglePlay()
                                }
                            }
                        }
                    }
                })*/
    }

    private fun checkUpdateInGooglePlay() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            when {
                it.installStatus() == InstallStatus.DOWNLOADING -> {
                    appUpdateManager.registerListener(installUpdateListener)
                }
                it.installStatus() == InstallStatus.DOWNLOADED -> {
                    listener.onFinishUpdate()
                }
                it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE -> {
                    when (updateType) {
                        UpdateType.MAJOR -> {
                            appUpdateManager.startUpdateFlowForResult(
                                    it,
                                    AppUpdateType.IMMEDIATE,
                                    activity,
                                    MAJOR_UPDATE_REQUEST_CODE
                            )
                        }
                        UpdateType.MINOR -> {
                            appUpdateManager.registerListener(installUpdateListener)
                            appUpdateManager.startUpdateFlowForResult(
                                    it,
                                    AppUpdateType.FLEXIBLE,
                                    activity,
                                    OPTIONAL_UPDATE_REQUEST_CODE
                            )
                        }
                        else -> {
                            showNativeUpdatePopup()
                        }
                    }
                }
                else -> {
                    showNativeUpdatePopup()
                }
            }
        }.addOnFailureListener {
            showNativeUpdatePopup()
        }
    }

    private fun showNativeUpdatePopup() {
        val updateClickListener = DialogInterface.OnClickListener { _, _ ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName()))
            try {
                activity.startActivity(intent)
                activity.finish()
            } catch (e: ActivityNotFoundException) {
                activity.showToast(
                        "Something went wrong")
            }
        }
        val disableClickListener = DialogInterface.OnClickListener { _, _ ->
            activity.savePreferenceValue(
                Constants.PREF_UPDATE_SKIPPED_VERSION, newAppVersion)
        }
        val btnDismissClickListener = DialogInterface.OnClickListener { _, _ -> }

        val alertDialog: AlertDialog = AlertDialog.Builder(activity)
                .setTitle("Update Available")
                .setMessage(String.format(
                    "Update %s is available to download. By downloading the latest update you will get the latest features, improvements and bug fixes.",
                    newAppVersion))
                .create()
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                "Update", updateClickListener)
        if (updateType == UpdateType.MAJOR) {
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)
        } else if (updateType == UpdateType.MINOR) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", updateClickListener)
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Next time", btnDismissClickListener)
        } else if (updateType == UpdateType.PATCH) {
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", updateClickListener)
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Next time", btnDismissClickListener)
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Skip", disableClickListener)
        }
        val skipVersion: String = activity.getPreferenceValue(
            Constants.PREF_UPDATE_SKIPPED_VERSION, "")
        if (newAppVersion != skipVersion) {
            alertDialog.show()
        }
    }

    private fun getUpdateType(currentVersion: String, newVersion: String): UpdateType? {
        if (currentVersion.matches(Regex(appVersionFormat))
                && newVersion.matches(Regex(appVersionFormat))) {
            val currentVersionSplit = currentVersion.split(".")
            val newVersionSplit = newVersion.split(".")

            for (index in 0..max(currentVersionSplit.size, newVersionSplit.size)) {
                val valueOfCurrentVersionIndex = if (index < currentVersionSplit.size) {
                    currentVersionSplit[index].toIntOrNull()
                } else null
                val valueOfNewVersionIndex = if (index < newVersionSplit.size) {
                    newVersionSplit[index].toIntOrNull()
                } else null

                if (valueOfCurrentVersionIndex != null && valueOfNewVersionIndex != null) {
                    if (index == 0 && valueOfNewVersionIndex > valueOfCurrentVersionIndex) {
                        return UpdateType.MAJOR
                    } else if (index == 1 && valueOfNewVersionIndex > valueOfCurrentVersionIndex) {
                        return UpdateType.MINOR
                    } else if (valueOfNewVersionIndex > valueOfCurrentVersionIndex) {
                        return UpdateType.PATCH
                    }
                }

            }
        }
        return null
    }

    fun installUpdate() {
        appUpdateManager.completeUpdate()
    }

    enum class UpdateType {
        PATCH, //i.e 1.0.0 to 1.0.1
        MINOR, //i.e 1.0.0 to 1.1.0
        MAJOR, //i.e 1.0.0 to 2.0.0
    }

    interface InAppUpdateListener {
        fun onStartUpdate()
        fun onProgressUpdate(percentage: String)
        fun onFinishUpdate()
    }

}