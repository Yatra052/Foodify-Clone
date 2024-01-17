package com.example.foodapplicationkot

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodapplicationkot.Model.UserModel
import com.example.foodapplicationkot.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var userName:String ?= null
    private lateinit var email :String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient:GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
           auth = Firebase.auth
        database = Firebase.database.reference
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)



        binding.loginButton.setOnClickListener{

            email = binding.emailadd.text.toString().trim()
            password = binding.passworrd.text.toString().trim()
            
            if (email.isBlank() || password.isBlank())
            {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show()
            }

            else
            {
                createUser()
                Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
            }


        }
        binding.donthavebtn.setOnClickListener{
            val intent = Intent(this,SignUpActivity::class.java )
            startActivity(intent)
        }

         binding.googleBtn.setOnClickListener{
             val signinIntent = googleSignInClient.signInIntent
             launcher.launch(signinIntent)
         }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
         result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign-in Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Sign in field", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun createUser() {
         auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
             task ->

             if (task.isSuccessful)
             {
                 saveUserdata()
                 val user = auth.currentUser
                 updateUi(user)
             }

             else
             {
                 auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                     task ->

                     if(task.isSuccessful)
                     {
                         val user = auth.currentUser
                         updateUi(user)
                     }
                     else
                     {
                         Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
                     }
                 }
                  }
         }

    }

    private fun saveUserdata() {

        email = binding.emailadd.text.toString().trim()
        password = binding.passworrd.text.toString().trim()

        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val  intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }
    }

            private fun updateUi(user: FirebaseUser?) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()


        }

    }



