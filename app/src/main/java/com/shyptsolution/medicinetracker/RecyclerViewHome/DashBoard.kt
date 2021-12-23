package com.shyptsolution.medicinetracker.RecyclerViewHome

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.R
import com.shyptsolution.medicinetracker.RoomDataBase.NoteViewModel
import com.shyptsolution.medicinetracker.RoomDataBase.RoomEntity
import com.shyptsolution.medicinetracker.add.AddNew
import com.shyptsolution.medicinetracker.add.SyncNow
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DashBoard : AppCompatActivity(), HomeAdapter.NotesAdapter, DashBoardAdapter.dashboard {
   lateinit var recyclerView:RecyclerView
    lateinit var viewModel: NoteViewModel
     var db=FirebaseFirestore.getInstance()
    var auth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        recyclerView=findViewById(R.id.dashboardrecyclerview)
        var adapter=DashBoardAdapter(this,this)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        supportActionBar?.title="DashBoar(All)"
//        Toast.makeText(this,"Inside Dashboard",Toast.LENGTH_LONG).show()
        viewModel= ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer {list->
            list?.let {
                adapter.updateList(it)
                var hashMap=HashMap<Int,Boolean>()
//                Toast.makeText(this,"Inside viewmoder",Toast.LENGTH_LONG).show()
//                Toast.makeText(this,"${it.size}",Toast.LENGTH_LONG).show()


//                Toast.makeText(this,"Size in Home "+it.size.toString(),Toast.LENGTH_LONG).show()
//                for (notes in it){
//                    if(!hashMap.containsKey(notes.id)){
//                        SaveData(this).SetAlarm(notes.hour,notes.minute,0,notes.medicineName)
//                        hashMap.set(notes.id,true)
//                    }
//
//                }


            }

        })
//        StoreOnline()
        val actionbar = supportActionBar
        actionbar!!.title = "DashBoard"
        actionbar.setDisplayHomeAsUpEnabled(true)

    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menu is MenuBuilder) (menu as MenuBuilder).setOptionalIconsVisible(true)
        val inflater: MenuInflater =menuInflater

        menuInflater.inflate(R.menu.dashboardmeny,menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item!=null){
            when(item.itemId){
                R.id.sync-> {
//                    var intent = Intent(this, AddNew::class.java)
//                    this.startActivity(intent)


                    val builder = AlertDialog.Builder(this)
                    //set title for alert dialog
                    builder.setTitle("Save To Cloud?")
                    //set message for alert dialog
                    builder.setMessage("This will save your data to cloud which can be accessed any time.")
                    builder.setIcon(R.drawable.ic_baseline_cloud_upload_24)

                    //performing positive action
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
//                Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
                        StoreOnline()
                        Toast.makeText(this,"Saved To Cloud",Toast.LENGTH_SHORT).show()
                    }
                    //performing cancel action
//            builder.setNeutralButton("Cancel"){dialogInterface , which ->
//                Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
//            }
                    //performing negative action
                    builder.setNegativeButton("Cancel"){dialogInterface, which ->
//                Toast.makeText(context,"clicked No",Toast.LENGTH_LONG).show()

                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                }
                R.id.download->{
//                    getfromdatabase()
//                    Toast.makeText(this,"Successfully Fetched From Cloud",Toast.LENGTH_SHORT).show()

                    val builder = AlertDialog.Builder(this)
                    //set title for alert dialog
                    builder.setTitle("Fetch From Cloud?")
                    //set message for alert dialog
                    builder.setMessage("This will fetch your saved data from cloud.")
                    builder.setIcon(R.drawable.ic_baseline_cloud_download_24)

                    //performing positive action
                    builder.setPositiveButton("Ok"){dialogInterface, which ->
//                Toast.makeText(context,"clicked yes",Toast.LENGTH_LONG).show()
                        getfromdatabase()
                        Toast.makeText(this,"Successfully Fetched From Cloud",Toast.LENGTH_SHORT).show()
                    }
                    //performing cancel action
//            builder.setNeutralButton("Cancel"){dialogInterface , which ->
//                Toast.makeText(context,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
//            }
                    //performing negative action
                    builder.setNegativeButton("Cancel"){dialogInterface, which ->
//                Toast.makeText(context,"clicked No",Toast.LENGTH_LONG).show()

                    }
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                }


            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Toast.makeText(this,"Back button clicked",Toast.LENGTH_LONG).show()
                startActivity(Intent(this,MainActivity::class.java))
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onItemClicked(note: RoomEntity){
        viewModel.deleteNote(note)
    }

    override fun onItemEdited(note: RoomEntity) {
        TODO("Not yet implemented")
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun StoreOnline(){
         var match=(Date().year.toString()+Date().month.toString()+Date().date.toString()+Date().hours.toString()+Date().minutes.toString()+Date().seconds.toString()+Calendar.MILLISECOND .toString())

        deletedatabase(match)
        uploadtodatabase()





        }

    fun getfromdatabase(){
        var hashmap=HashMap<Int,RoomEntity>()
        var email=auth.currentUser!!.email.toString()
//        Toast.makeText(this,"${email}",Toast.LENGTH_SHORT).show()
        db.collection(email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    var idd=document.get("id").toString().toInt()
                    val medicineName=document.getString("medicineName")
                    val time=document.getString("time")
                    var hour=document.get("hour").toString().toInt()
                    var minute=document.get("minute").toString().toInt()
                    var dose=document.getString("dose")
                    var stock=document.getString("stock")
                    var monday=document.getBoolean("monday").toString().toBoolean()
                    var tuesday=document.getBoolean("tuesday").toString().toBoolean()
                    var wednesday=document.getBoolean("wednesday").toString().toBoolean()
                    var thursday=document.getBoolean("thursday").toString().toBoolean()
                    var friday=document.getBoolean("friday").toString().toBoolean()
                    var saturday=document.getBoolean("saturday").toString().toBoolean()
                    var sunday=document.getBoolean("sunday").toString().toBoolean()
//
//                    Toast.makeText(this,"id is ${idd}",Toast.LENGTH_SHORT).show()
                    var note=RoomEntity(medicineName.toString(),time.toString(),hour,minute,dose.toString(),stock.toString(),monday,tuesday,wednesday
                    ,thursday,friday,saturday,sunday)
//                    hashmap.set(idd,note)
                    hashmap[idd]=note
                }
//                Toast.makeText(this,"${hashmap.size} hashmap ka size",Toast.LENGTH_SHORT).show()
                var alreadyexist=HashMap<Int,Boolean>()
                viewModel.allNotes.observe(this, Observer {list->
                    list?.let {
//                        Toast.makeText(this, "${it.size} it ka size", Toast.LENGTH_SHORT).show()

                        // Create a new user with a first and last name

                        for (notes in it) {
                            alreadyexist.set(notes.id,true)
//                    val user = hashMapOf(
//                        "id" to notes.id,
//                        "medicineName" to notes.medicineName.toString(),
//                        "time" to notes.time.toString(),
//                        "hour"  to notes.hour,
//                        "minute"  to notes.minute,
//                        "dose" to notes.dose.toString(),
//                        "stock" to notes.stock,
//                        "monday" to notes.monday,
//                        "tuesday" to notes.tuesday,
//                        "wednesday" to notes.wednesday,
//                        "thursday" to notes.thursday,
//                        "friday" to notes.friday,
//                        "saturday" to notes.saturday,
//                        "sunday" to notes.sunday,
//
//                        )


                        }
                    }

                })
//                Toast.makeText(this,"${alreadyexist.size} already exist ka size",Toast.LENGTH_SHORT).show()

                for ((key, value) in hashmap.entries) {
                    if(!alreadyexist.containsKey(key)){
                        viewModel.insertNote(value)
//                        Toast.makeText(this,"${key}",Toast.LENGTH_SHORT).show()
                    }

                }
//                Toast.makeText(this,"${hashmap.size} hashmap ka size",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }




    }

    fun deletedatabase(match:String){
        db.collection("${auth.currentUser!!.email}")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id<match){
                        db.collection("${auth.currentUser!!.email}").document(document.id).delete()
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
        return

    }

    fun uploadtodatabase(){
        viewModel.allNotes.observe(this, Observer {list->
            list?.let {
//                Toast.makeText(this, "${it.size} it ka size", Toast.LENGTH_SHORT).show()

                // Create a new user with a first and last name

                for (notes in it) {

                    val user = hashMapOf(
                        "id" to notes.id,
                        "medicineName" to notes.medicineName,
                        "time" to notes.time,
                        "hour"  to notes.hour,
                        "minute"  to notes.minute,
                        "dose" to notes.dose,
                        "stock" to notes.stock,
                        "monday" to notes.monday,
                        "tuesday" to notes.tuesday,
                        "wednesday" to notes.wednesday,
                        "thursday" to notes.thursday,
                        "friday" to notes.friday,
                        "saturday" to notes.saturday,
                        "sunday" to notes.sunday,

                        )
                    var number = notes.id.toString()
//                    db.collection("${auth.currentUser!!.email}")
////                        .document(number)
//                        .add(user)
//                        .addOnSuccessListener { documentReference ->
//                            Log.d("TAG", "DocumentSnapshot added with ID:")
//
//                        }
//                        .addOnFailureListener { e ->
//                            Log.w("TAG", "Error adding document", e)
//                        }
                    db.collection("${auth.currentUser!!.email}").document("${Date().year}${Date().month}${Date().date}${Date().hours}${Date().minutes}${Date().seconds}${Date().time}").set(user)
//                    db.collection("${auth.currentUser!!.email}").document("${Date().year}${Date().month}${Date().date}${Date().hours}${Date().minutes}${Date().seconds}")
//                        .collection("${number}").add(user)

                }
            }

        })
    }


}