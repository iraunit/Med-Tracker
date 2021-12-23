package com.shyptsolution.medicinetracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shyptsolution.medicinetracker.Alarm.Notification
import com.shyptsolution.medicinetracker.Alarm.SaveData
import com.shyptsolution.medicinetracker.Login.Login
import com.shyptsolution.medicinetracker.RecyclerViewHome.DashBoard
import com.shyptsolution.medicinetracker.RecyclerViewHome.DashBoardAdapter
import com.shyptsolution.medicinetracker.RecyclerViewHome.DashBoardData
import com.shyptsolution.medicinetracker.RecyclerViewHome.HomeAdapter
import com.shyptsolution.medicinetracker.RoomDataBase.BaseFragment
import com.shyptsolution.medicinetracker.RoomDataBase.DataBase
import com.shyptsolution.medicinetracker.RoomDataBase.NoteViewModel
import com.shyptsolution.medicinetracker.RoomDataBase.RoomEntity
import com.shyptsolution.medicinetracker.add.AddNew
import com.shyptsolution.medicinetracker.add.PopUpTime
import com.shyptsolution.medicinetracker.add.SyncNow
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : BaseFragment(), HomeAdapter.NotesAdapter, DashBoardAdapter.dashboard {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleAuth: GoogleSignInClient
    private var backPressedTime = 0L
    private var searchClicked = false
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var homeRecyclerView: RecyclerView
    lateinit var MedList: ArrayList<DashBoardData>
    lateinit var viewModel: NoteViewModel
    var db = FirebaseFirestore.getInstance()
    lateinit var currentusrEmail: String

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var driver: DrawerLayout = findViewById(R.id.mainactivity)
        toggle = ActionBarDrawerToggle(this, driver, R.string.open, R.string.close)
        driver.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var floatingActionButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, AddNew::class.java))
        }
        //Recycler View Implemantation
        homeRecyclerView = findViewById(R.id.recyclerview)
        // Create a new user with a first and last name
        supportActionBar?.title="Medicines To Take Today"

//        launch {
//            val newnote=DataBase(this@MainActivity).getDao().getAllNotes()
////            Toast.makeText(this@MainActivity, "note list size "+newnote.size.toString(),Toast.LENGTH_SHORT).show()
//
//
//        }
        homeRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        homeRecyclerView.setHasFixedSize(true)
        var adapter = HomeAdapter(this, this)
        homeRecyclerView.adapter = adapter
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        viewModel.todayNotes.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
                var hashMap = HashMap<Int, Boolean>()
                var hour = Date().hours
                var minutes = Date().minutes
                for (notes in it) {
                    if (hour <= notes.hour && minutes <= notes.minute) {
//                        Toast.makeText(this,"${notes.hour}+${notes.minute}",Toast.LENGTH_SHORT).show()
//                        updatesotck(notes.id, notes.stock.toInt())
                        SaveData(this).SetAlarm(
                            notes.hour,
                            notes.minute,
                            0,
                            notes.medicineName
                        )
                    }


                }
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

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.clientid))
            .requestProfile()
            .build()

        googleAuth = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
        var navigationView = findViewById<NavigationView>(R.id.navmenu)
        var navheader = navigationView.getHeaderView(0)
        var email = navheader.findViewById<TextView>(R.id.emailid)
        var username = navheader.findViewById<TextView>(R.id.name)
        var userPhoto = navheader.findViewById<ImageView>(R.id.userimage)
        email.setText(auth.currentUser!!.email)
        username.setText(auth.currentUser!!.displayName)
        Picasso.get().load(auth.currentUser!!.photoUrl).into(userPhoto)
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    googleAuth.signOut()
                    googleAuth.revokeAccess()
                    auth.signOut()
                    startActivity(Intent(this, Login::class.java))
                }

                R.id.exitapp -> {
                    finishAffinity()
                }


                R.id.dashboard -> {
                    startActivity(Intent(this, DashBoard::class.java))
                }
                R.id.buymedicine -> {
                    val k = Intent(Intent.ACTION_VIEW)
                    k.data = Uri.parse("https://pharmeasy.in/")
                    startActivity(k)
                }
//                R.id.sync -> {
//                    val popTime = SyncNow()
//                    var fgm = supportFragmentManager
//                    popTime.show(fgm, "Sync Now")
//                }


                else -> throw IllegalStateException("Unexpected value: " + item.itemId)
            }
            true
        })
        createNotificationChannel()
//            onrepeat(this)
        SaveData(this).SetSnooze(0, 0)

    }


    //BAck button close function
    override fun onBackPressed() {
        if (searchClicked) {
            super.onBackPressed()
            searchClicked = false
        }
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
        } else {
            Toast.makeText(this, "Press Back Again To Exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()

    }

    //Adding option in menu
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater: MenuInflater =menuInflater
//        menuInflater.inflate(R.menu.home_menu,menu)
//        return super.onCreateOptionsMenu(menu)
//    }
    //adding click function on navigation bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

//        if(item!=null){
//            when(item.itemId){
//                R.id.dashboard-> {
//                    var intent = Intent(this, AddNew::class.java)
//                    this.startActivity(intent)
//                }
//                R.id.medication->{
//                    searchClicked=true
//                }
//                R.id.logout->{
//
//                }
//            }
//        }

        return super.onOptionsItemSelected(item)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("RaunitVerma", name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)

//            channel.setSound(Uri.parse("android.resource://" + this.packageName + "/" + R.raw.ringtone),AudioAttributes.USAGE_ALARM)
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onItemClicked(note: RoomEntity) {
        viewModel.deleteNote(note)
    }

    override fun onItemEdited(note: RoomEntity) {
        var stock=note.stock.toInt()-1
        viewModel.updatestock(stock.toString(),note.id)
        Toast.makeText(this,"${stock}",Toast.LENGTH_SHORT).show()

    }
    override fun updateday(note:RoomEntity){
//        viewModel.updateday(note.id)

    }

    fun finishApp() {
        finishAffinity()
    }

    fun updatesotck(stock: Int, id: Int) {
        Toast.makeText(this,"Inside upate",Toast.LENGTH_SHORT).show()
//        var viewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        ).get(NoteViewModel::class.java)

//        viewModel.updatestock(stock, id)
//    }
    }
}