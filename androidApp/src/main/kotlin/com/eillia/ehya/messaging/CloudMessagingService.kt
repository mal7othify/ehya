/*
 * Copyright 2026 Maryam Alhuthayfi
 * SPDX-License-Identifier: MIT
 */

package com.eillia.ehya.messaging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger
import com.eillia.ehya.MainActivity
import com.eillia.ehya.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CloudMessagingService : FirebaseMessagingService() {
  override fun onNewToken(token: String) {
    Logger.d { "FCM token refreshed" }
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    val notification = remoteMessage.notification ?: return
    if (!canPostNotifications()) return
    createChannel()

    val openApp =
      PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
      )
    val built =
      NotificationCompat
        .Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(notification.title)
        .setContentText(notification.body)
        .setStyle(NotificationCompat.BigTextStyle().bigText(notification.body))
        .setContentIntent(openApp)
        .setAutoCancel(true)
        .build()
    notificationManager.notify(NOTIFICATION_ID, built)
  }

  private val notificationManager: NotificationManager
    get() = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

  private fun canPostNotifications(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
      ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
      PackageManager.PERMISSION_GRANTED

  private fun createChannel() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) return
    notificationManager.createNotificationChannel(
      NotificationChannel(
        CHANNEL_ID,
        getString(R.string.notification_channel),
        NotificationManager.IMPORTANCE_DEFAULT
      )
    )
  }

  private companion object {
    /** Kept from the original app so existing user channel settings still apply. */
    const val CHANNEL_ID = "1"
    const val NOTIFICATION_ID = 0
  }
}
