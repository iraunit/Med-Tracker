package com.shyptsolution.medicinetracker.Alarm

import android.app.Application
import android.app.Notification
import android.app.Notification.DEFAULT_ALL
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.NotificationManager.IMPORTANCE_MAX
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationManagerCompat
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.R
import com.shyptsolution.medicinetracker.RoomDataBase.BaseFragment
import com.shyptsolution.medicinetracker.add.AddNew
import com.shyptsolution.medicinetracker.add.PopUpTime
import kotlinx.coroutines.launch
import android.media.Ringtone
import android.os.AsyncTask
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shyptsolution.medicinetracker.RoomDataBase.NoteViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.util.*
import javax.security.auth.callback.Callback


class Notification:BaseFragment() {

    val NOTIFIYTAG="new request"
    @RequiresApi(Build.VERSION_CODES.P)
    fun Notify(context: Context, message:String, number:Int){
//        Toast.makeText(context,"${number} in Notify",Toast.LENGTH_LONG).show()
//        launch {
            var alarmSound = RingtoneManager. getDefaultUri (RingtoneManager. TYPE_NOTIFICATION)
            val snoozeIntent = Intent(context, myBroadcastReceiver::class.java).apply {
                action ="Snooze"
                putExtra("EXTRA_NOTIFICATION_ID", "${ number }")
                putExtra("MedName", "${ message }")
            }
        snoozeIntent.putExtra("Number","${number}")
        val snoozeIntent1 = Intent(context, myBroadcastReceiver::class.java).apply {
            action ="Snooze1"
            putExtra("EXTRA_NOTIFICATION_I", "${ number }")
            putExtra("MedNam",message)
        }
            val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//            vibe.vibrate(1000)
//        vibe.vibrate(500,alarmSound)
            val snoozePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(context, 0, snoozeIntent, 0)
        val snoozePendingIntent1: PendingIntent =
            PendingIntent.getBroadcast(context, 0, snoozeIntent1, 0)
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val fullScreenIntent = Intent(context, PopUpTime::class.java)
            val fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val builder= NotificationCompat.Builder(context,"RaunitVerma")
                .setDefaults(NotificationCompat.PRIORITY_MAX)
                .setContentTitle("Take "+message+ " Now")
//            .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("Don't Forget To Take The Medicine on Time"))
                .setNumber(number)
//                .setSound(alarmSound)
                .setVibrate(longArrayOf(100, 1000, 1000, 100, 1000))
                .setSound( Uri.parse("android.resource://" + context.packageName + "/" + R.raw.ringtone))
                .setSmallIcon(R.drawable.ic_baseline_add_shopping_cart_24)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_baseline_add_24, "Snooze",
                    snoozePendingIntent)
                .addAction(R.drawable.ic_baseline_add_24, "Long Snooze",
                    snoozePendingIntent1)




//        var builder = NotificationCompat.Builder(context,"RaunitVerma")
//            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
//            .setContentTitle("textTitle")
//            .setContentText("textContent")
//            .setPriority(NotificationCompat.PRIORITY_MAX)

            val nm=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
                nm.notify(NOTIFIYTAG, number, builder.build())
//            Toast.makeText(context,"In first", Toast.LENGTH_LONG).show()
                try {

                    var rawPathUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.ringtone);
                    var  r =RingtoneManager. getRingtone(context, rawPathUri)
                    r.play()

                } catch (e: Exception) {
                    Toast.makeText(context,"${e}", Toast.LENGTH_LONG).show()

                    e.printStackTrace()
                }
            }else{
                nm.notify(NOTIFIYTAG.hashCode(), builder.build())
//            Toast.makeText(context,"In second",Toast.LENGTH_LONG).show()

            }






    }
     fun dismiss(context: Context, number: Int){
         val nm=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//         Toast.makeText(context,"${number}",Toast.LENGTH_SHORT).show()
         nm.cancel(number)
//         r.stop()
     }




}