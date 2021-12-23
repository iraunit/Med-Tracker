package com.shyptsolution.medicinetracker.add

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.shyptsolution.medicinetracker.Alarm.Notification
import com.shyptsolution.medicinetracker.Alarm.SaveData
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.R
import com.shyptsolution.medicinetracker.RecyclerViewHome.HomeAdapter
import com.shyptsolution.medicinetracker.RoomDataBase.BaseFragment
import com.shyptsolution.medicinetracker.RoomDataBase.DataBase
import com.shyptsolution.medicinetracker.RoomDataBase.NoteViewModel
import com.shyptsolution.medicinetracker.RoomDataBase.RoomEntity
import kotlinx.coroutines.launch
import java.util.*

class AddNew : BaseFragment(),HomeAdapter.NotesAdapter {
    lateinit var adapter:HomeAdapter
      lateinit  var medName:EditText
    lateinit  var dose:EditText
    lateinit  var stock:EditText
//    lateinit  var time:TextView
    lateinit  var mon:CheckBox
    lateinit  var tue:CheckBox
    lateinit var wed:CheckBox
    lateinit  var thu:CheckBox
    lateinit  var fri:CheckBox
    lateinit var sat:CheckBox
    lateinit var sun:CheckBox
     var hour:Int=0
     var minute:Int=0
    lateinit var spinner:TimePicker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new)
//        var button=findViewById<Button>(R.id.settime)
//        button.setOnClickListener {
//            selectTime()
//        }
        supportActionBar?.title="Add New"
                 medName=findViewById<EditText>(R.id.medicineNameinput)
         dose=findViewById<EditText>(R.id.doseinput)
         stock=findViewById<EditText>(R.id.stockinput)

         mon=findViewById<CheckBox>(R.id.mon)
         tue=findViewById<CheckBox>(R.id.tus)
         wed=findViewById<CheckBox>(R.id.wed)
         thu=findViewById<CheckBox>(R.id.thu)
         fri=findViewById<CheckBox>(R.id.fri)
         sat=findViewById<CheckBox>(R.id.sat)
         sun=findViewById<CheckBox>(R.id.sun)
        spinner=findViewById(R.id.spinner)
    }

    private fun selectTime() {
        val popTime=PopUpTime()
        var fgm=supportFragmentManager
        popTime.show(fgm,"Are You Sure?")
    }

//    fun setTime(Hours:Int,Minutes:Int){
//        var time=findViewById<TextView>(R.id.time)
//        if(Minutes==0 && Hours==0){
//            time.setText("00:00")
//
//        }
//       else if (Hours==0){
//            time.setText("00"+":"+Minutes.toString())
//
//        }
//        else if(Minutes==0){
//            time.setText(Hours.toString()+":00")
//
//        }
//
//        else{
//            time.setText(Hours.toString()+":"+Minutes.toString())
//
//        }
//    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater =menuInflater
        menuInflater.inflate(R.menu.addnewmenu,menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){
            when(item.itemId){
                R.id.addtask-> {
                    if(medName.text.toString().isEmpty()){
                        medName.error="Medicine Name Required"
                        medName.requestFocus()
                    }
                    else if(stock.text.toString().isEmpty()){
                        stock.error="Medicine Name Required"
                        stock.requestFocus()
                    }
                    else{
                        hour=spinner.hour
                        minute=spinner.minute
                        var time:String=""
                        if(minute==0 && hour==0){
            time=("00:00")

        }
       else if (hour==0){
            time=("00"+":"+minute.toString())

        }
        else if(minute==0){
            time=(hour.toString()+":00")

        }
        else if(minute<10 && hour<10){
            time="0${hour}:0${minute}"
                        }

        else{
            time=(hour.toString()+":"+minute.toString())

        }
                        var medicine=RoomEntity(medName.text.toString(),time.toString(),hour,minute,dose.text.toString(),stock.text.toString(),mon.isChecked,tue.isChecked,wed.isChecked,thu.isChecked,
                            fri.isChecked,sat.isChecked,sun.isChecked)


                    var viewModel= ViewModelProvider(this,
                        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
                        NoteViewModel::class.java)
                        viewModel.insertNote(medicine)
//                        launch {
//                            this@AddNew.let {
//                                DataBase(this@AddNew).getDao().addNote(medicine)
////                Toast.makeText(it,"Saved to database",Toast.LENGTH_LONG).show()
//                            }
//                        }
//                        setAlarm(hour,minute,medName.text.toString())

                        startActivity(Intent(this,MainActivity::class.java))
                    }


                }
                R.id.cancel->{


                    val builder = AlertDialog.Builder(this)
                    //set title for alert dialog
                    builder.setTitle("Are You Sure?")
                    //set message for alert dialog
                    builder.setMessage("Tap Yes To Discard This Reminder.")
                    builder.setIcon(R.drawable.ic_baseline_delete_24)

                    //performing positive action
                    builder.setPositiveButton("Yes"){dialogInterface, which ->
//                Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    }
                    //performing cancel action
//            builder.setNeutralButton("Cancel"){dialogInterface , which ->
//                Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
//            }
                    //performing negative action
                    builder.setNegativeButton("No"){dialogInterface, which ->
//                Toast.makeText(context,"clicked No",Toast.LENGTH_LONG).show()
                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
//                    selectTime()
//                    var intent =
                //                    Intent(this, MainActivity::class.java)
//                    this.startActivity(intent)

                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

     fun onItemEdit(note: RoomEntity) {


//        startActivity(Intent(this   ,AddNew::class.java))
//        Toast.makeText(this,"Edited",Toast.LENGTH_LONG).show()
        medName.setText("Hello")
//        time.text.toString()
//        dose.text.toString(),stock.text.toString(),mon.isChecked,tue.isChecked,wed.isChecked,thu.isChecked,
//        fri.isChecked,sat.isChecked,sun.isChecked

//                val notifyme= Notification()
//        notifyme.Notify(context,"Hello",3)
//        super.onItemClicked(note)
    }

    fun setAlarm(hour:Int,Minutes: Int,medName:String){
//        Toast.makeText(this,"Snoozed for Five Minutes",Toast.LENGTH_LONG).show()
//        SaveData(this).SetAlarm(hour,Minutes, Calendar.DAY_OF_WEEK,medName)

    }
}