package com.wcupa.assignmentNotifier

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.wcupa.assignmentNotifier.notifications.EventCheckWorker
import com.wcupa.assignmentNotifier.ui.theme.AssignmentTheme
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            AssignmentTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AssignmentApp()
                }
            }
        }

    //Requests Notification Permissions
        //If Notifications aren't enabled, open dialog box
        if(!areNotificationsEnabled(this)) {
            val builder = AlertDialog.Builder(this@MainActivity)

            builder.setCancelable(true)
            builder.setTitle("Enable Notifications")
            builder.setMessage("Please enable notifications to access full functionality of this app.")

            //Closes out dialog box
            builder.setNegativeButton(
                "Close"
            ) { dialog, _ -> dialog.cancel() }

            //Navigates to settings page
            builder.setPositiveButton(
                "Enable Notifications"
            ) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + applicationContext.packageName)
                startActivity(intent)
            }

            builder.show()
        }

    //For WorkManager to manage Notifications
        // Create periodic work request
        val periodicWorkRequest = PeriodicWorkRequestBuilder<EventCheckWorker>(1, TimeUnit.DAYS) //
            .build()

        // Enqueue periodic work
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "EventCheckWorker",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            periodicWorkRequest
        )

    }

    //Checks if notifications are enabled
    private fun areNotificationsEnabled(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }
}
