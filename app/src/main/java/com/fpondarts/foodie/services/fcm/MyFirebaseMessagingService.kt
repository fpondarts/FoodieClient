package com.fpondarts.foodie.services.fcm

import android.content.ContentValues.TAG
import android.util.Log
import com.fpondarts.foodie.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.kodein.di.android.x.kodein
import android.R
import android.R.attr.colorPrimaryDark
import android.os.Build
import android.media.RingtoneManager
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.app.PendingIntent
import android.content.Intent
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationManager
import com.google.firebase.messaging.RemoteMessage
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.content.Context
import android.graphics.Color
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.fpondarts.foodie.ui.auth2.AuthActivity
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MyFirebaseMessagingService: FirebaseMessagingService(), KodeinAware {

    override val kodein by kodein()

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

    }

    private val ADMIN_CHANNEL_ID = "admin_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent(this, AuthActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.sym_def_app_icon
        )


        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["message"])
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setLargeIcon(largeIcon)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationID, notificationBuilder.build())
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to devie notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.GREEN
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }

}