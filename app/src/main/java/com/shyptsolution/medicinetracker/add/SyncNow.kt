package com.shyptsolution.medicinetracker.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.R
import com.shyptsolution.medicinetracker.RecyclerViewHome.DashBoard
import com.shyptsolution.medicinetracker.RecyclerViewHome.DashBoardAdapter
import kotlinx.coroutines.launch

class SyncNow:DialogFragment() {
    private lateinit var auth: FirebaseAuth
    var  db= FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var myView=inflater.inflate(R.layout.syncnow,container,false)
        var fetchnow=myView.findViewById<Button>(R.id.fetch)
        var savetocloud=myView.findViewById<Button>(R.id.savetocloud)
        fetchnow.setOnClickListener {


        }
        savetocloud.setOnClickListener {
            // Create a new user with a first and last name
            val user = hashMapOf(
                "first" to "Ada",
                "last" to "Lovelace",
                "born" to 1815
            )
//            MainActivity().SaveToCloud(requireActivity().applicationContext)

//            var intent= Intent(activity,DashBoard()::class.java)
//            startActivity(intent)
            DashBoard().StoreOnline()
//// Add a new document with a generated ID
//            db.collection("${userEmail }}")
//                .add(user)
//                .addOnSuccessListener { documentReference ->
//                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
//                }
//                .addOnFailureListener { e ->
//                    Log.w("TAG", "Error adding document", e)
//                }
        }


        return myView
    }
}