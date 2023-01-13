package karma.converter.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import karma.converter.R
import karma.converter.utils.Constants
import karma.converter.utils.UserStateManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONObject
import timber.log.Timber


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val gson = Gson()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        try {
            val response = remoteMessage.data
            val jsonObject = JSONObject(response as Map<*, *>)
            Timber.d("Message ${jsonObject.toString()}")
            val title = jsonObject.getString("title")
            val bodyMessage = JSONObject(jsonObject.getString("data"))
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.e(e)
        }
    }

    private fun showNotification(mainIntent: Intent, title: String) {
        mainIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this, Constants.PUSH_NOTIFICATION_CHANNEL_ID)

        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL)
        notificationBuilder.setSound(defaultSoundUri)
        notificationBuilder.priority = NotificationCompat.PRIORITY_MAX
        notificationBuilder.setContentIntent(pendingIntent)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.color = ContextCompat.getColor(this, R.color.colorPrimary)
        val notificationId = 2

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("token = %s", token)
        UserStateManager.saveFirebaseToken(token)
    }
}