package com.example.pushnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.pushnotification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "ANDROID"
    private val CHANNEL_NAME = "RAHUL Android"
    private val CHANNEL_DESCRIPTION = "ANDROID PUSH NOTIFICATION"
    private val link1 = "https://www.rentainance.com/blogs"
    private val link2 = "https://www.rentainance.com/"
    private var largeIcon: Bitmap?=null
    lateinit var pendingIntent1: PendingIntent
    lateinit var pendingIntent2: PendingIntent
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

          largeIcon = getBitmapFromVectorDrawable(R.drawable.ic_android_black_24dp)



    binding.btn1.setOnClickListener {
        val titleText = binding.et1.text.toString().trim()
        val contentText = binding.et2.text.toString().trim()

        if (titleText.isNotEmpty() && contentText.isNotEmpty()) {
            // By invoking the notificationChannel() function we
            // are registering our channel to the System
            notificationChannel()

            // Building the notification
            val nBuilder = buildNotification()

            // finally notifying the notification
            val nManager = NotificationManagerCompat.from(this)

            if (ActivityCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS")
                != PackageManager.PERMISSION_GRANTED
            ) {

                Toast.makeText(this, "Please Allowed Permisson From System", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                nManager.notify(1, nBuilder)
                Toast.makeText(this, "Notification Success", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            Toast.makeText(this,"Title & Description may not Empty",Toast.LENGTH_SHORT).show()
        }
    }

    }

    // Creating the notification
    private fun buildNotification(): Notification {

        // Making intent1 to open the web home page
        val intent1 = openIntent(link1)

        // Making intent2 to open The web contribution page
        val intent2 = openIntent(link2)

        // Making intent2 to open App Activity
        val intent3 = openActivityIntent()


        // page after clicking the Notification
        pendingIntent1 = PendingIntent.getActivity(this, 0, intent3, PendingIntent.FLAG_IMMUTABLE)

        // page after clicking the actionButton of the notification
        pendingIntent2 = PendingIntent.getActivity(this, 2, intent2, PendingIntent.FLAG_IMMUTABLE)
        val nBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(binding.et1.text.toString()) // adding notification Title
            .setContentText(binding.et2.text.toString()) // adding notification Text
            .setSmallIcon(R.drawable.baseline_notifications_none_24) // adding notification SmallIcon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // adding notification Priority
            .setContentIntent(pendingIntent1)  // making the notification clickable
            .setAutoCancel(true)
            .addAction(0, "LET CONTRIBUTE", pendingIntent2) // adding action button
            .setLargeIcon(largeIcon)  // adding largeIcon
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(null))  // making notification Expandable
            .build()
        return nBuilder
    }

    // Creating the notification channel
    private fun notificationChannel() {
        // check if the version is equal or greater
        // than android oreo version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // creating notification channel and setting
            // the description of the channel
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = CHANNEL_DESCRIPTION
            }
            // registering the channel to the System
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Function to get a Bitmap from a vector drawable
    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        val vectorDrawable = VectorDrawableCompat.create(resources, drawableId, theme)
        vectorDrawable?.let {
            val bitmap = Bitmap.createBitmap(
                it.intrinsicWidth,
                it.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            return bitmap
        }
        return null
    }


    // an Implicit Intent to open a webpage
    private fun openIntent(link: String): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(link)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return intent


    }
    // an Explicit Intent to open a webpage
    private fun openActivityIntent(): Intent {
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        return intent
    }
}
