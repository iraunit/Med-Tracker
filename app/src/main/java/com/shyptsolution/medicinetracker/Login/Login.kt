package com.shyptsolution.medicinetracker.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shyptsolution.medicinetracker.MainActivity
import com.shyptsolution.medicinetracker.R

class Login : AppCompatActivity() {
    companion object{
        const val CONST_SIGN_IN=34
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleAuth:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        auth= FirebaseAuth.getInstance()
        var GoogleSignInbutton=findViewById<ImageView>(R.id.googlesignin)
        GoogleSignInbutton.setOnClickListener {
            GoogleSignIN()
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.clientid))
            .requestEmail()
            .build()

        googleAuth = GoogleSignIn.getClient(this, gso)



    }
    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }

    private fun GoogleSignIN() {
        val account= GoogleSignIn.getLastSignedInAccount(this)
        if(account==null){
            val signInIntent=googleAuth.signInIntent
            startActivityForResult(signInIntent, CONST_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CONST_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val accout=task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(accout.idToken)
            }
            catch (e:ApiException){
                Toast.makeText(this,"${e}",Toast.LENGTH_LONG).show()
            }
        }
    }
}