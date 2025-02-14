package com.ourapp.ise_app_dev

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
                Log.e("GeofenceReceiver", "Error: $errorMessage")
                return
            }
        }

        val transition = geofencingEvent?.geofenceTransition
        Log.d("GeofenceReceiver", "Geofence transition: $transition")

        if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeredGeofences = geofencingEvent.triggeringGeofences
            if (triggeredGeofences != null) {
                for (geofence in triggeredGeofences) {
                    val iitName = geofence.requestId
                    Log.d("GeofenceReceiver", "Entered geofence: $iitName")
                    sendNotification(context, iitName)
                }
            }
        }
    }

    private fun sendNotification(context: Context, iitName: String) {
        val notificationIntent = Intent(context, MapActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "geofence_channel"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Geofence Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Geofence Alert")
            .setContentText("You have entered the area of $iitName.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}