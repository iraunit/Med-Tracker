package com.shyptsolution.medicinetracker.Alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.RecyclerViewHome.DashBoard
import com.shyptsolution.medicinetracker.RoomDataBase.DataBase
import com.shyptsolution.medicinetracker.RoomDataBase.NoteViewModel
import com.shyptsolution.medicinetracker.RoomDataBase.RoomEntity
import com.shyptsolution.medicinetracker.add.AddNew
import kotlinx.coroutines.delay
import java.util.*

class myBroadcastReceiver:BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context, intent: Intent?) {
//        Toast.makeText(context,"Alarm Recived",Toast.LENGTH_LONG).show()

        if(intent!!.action.equals(".Alarm")){
            val notifyme=Notification()
           var message= intent.getStringExtra("Message").toString()
            if(message==null){
                message=""
            }
                notifyme.Notify(context, message,getNumber())
//            var id=intent.getStringExtra("id")!!.toInt()
//            var stock=intent.getStringExtra("stock")!!.toInt()
//            MainActivity().updatesotck(id,stock)

        }
        else if(intent.action.equals("Snooze")){
            Toast.makeText(context,"Snoozed For Two Minutes",Toast.LENGTH_LONG).show()
            val MainAct=SaveData(context)
            var message=intent.getStringExtra("MedName")

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if(Build.VERSION.SDK_INT>=23) {
                        MainAct.justset(Calendar.HOUR_OF_DAY, Calendar.MINUTE+2,Date().day,"Your Medicine")
                    }
                }
            }, 60000)
            var number=intent.getIntExtra("EXTRA_NOTIFICATION_ID",0)
            Notification().dismiss(context,number )
        }
        else if(intent.action.equals("Snooze1")){
            Toast.makeText(context,"Snoozed For Five Minutes",Toast.LENGTH_LONG).show()

            val MainAct=SaveData(context)
            var message=intent.getStringExtra("MedNam")

            var number=intent.getIntExtra("EXTRA_NOTIFICATION_I",0)
            Notification().dismiss(context,number )
//            Toast.makeText(context,"${number} in snooze 1",Toast.LENGTH_SHORT).show()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if(Build.VERSION.SDK_INT>=23) {
                        MainAct.justset(Calendar.HOUR_OF_DAY, Calendar.MINUTE+2,Date().day,"Your Medicine")
                    }
                }
            }, 240000)
        }
        else if(intent.action.equals("Repeat")){
//            MainActivity().setAllToday(context)
//            MainActivity().onrepeat(context)
//            context.startActivity(intent)
                var min=Date().minutes
            var hour=Date().hours
            if(min==0 && hour==0){

                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        var inttent=Intent(context,MainActivity::class.java)
                        inttent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(inttent)
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                Toast.makeText(context,"Completed",Toast.LENGTH_SHORT).show()

                                MainActivity().finishApp()
                            }
                        }, 2000)
                    }
                }, 1000)



            }


//            Toast.makeText(context,"Inside Repeat",Toast.LENGTH_SHORT).show()
        }
        else if(intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            Toast.makeText(context,"Boot Completed",Toast.LENGTH_LONG).show()

            Timer().schedule(object : TimerTask() {

                override fun run() {
                    var inttent=Intent(context,MainActivity::class.java)
                    inttent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(inttent)
//                    Timer().schedule(object :TimerTask(){
//                        override fun run() {
//                            MainActivity().finishApp()
//                        }
//
//                    }, 2000)
                }
            }, 1000)

        }
        else{
            Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show()
        }
    }

    fun getNumber(): Int = (Date().time / 1000L % Integer.MAX_VALUE).toInt()


}