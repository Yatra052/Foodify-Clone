package com.example.foodapplicationkot

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodapplicationkot.Model.UserModel
import com.example.foodapplicationkot.databinding.ActivityLoginBinding
import com.example.foodapplicationkot.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class SignUpActivity : AppCompatActivity() {


    private lateinit var email: String
    private lateinit var  password:String
    private lateinit var userName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding :ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        auth = Firebase.auth
        database = Firebase.database.reference

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

          binding.createButton.setOnClickListener{
              userName = binding.username.text.toString().trim()
              email = binding.emailadd.text.toString().trim()
              password = binding.passworrd.text.toString().trim()

              if(email.isBlank() || password.isBlank()|| userName.isBlank())
              {
                  Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
              }
              else
              {
                  createAccount(email, password)
              }
          }


        binding.alreadyhavebtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.googleBtn.setOnClickListener{
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }
    }

    private val launcher  = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
       result ->

        if (result.resultCode == Activity.RESULT_OK)
        {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            if(task.isSuccessful)
            {
                val account:GoogleSignInAccount? =task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                    task ->

                    if (task.isSuccessful)
                    {
                        Toast.makeText(this, "Sign-in Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }

                    else
                    {
                        Toast.makeText(this, "Sign in field", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            else
            {
                Toast.makeText(this, "Sign in field", Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun createAccount(email: String, password: String) {
       auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
           task ->
           if (task.isSuccessful)
           {
               Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
               saveUserData()
               startActivity(Intent(this, LoginActivity::class.java))
               finish()
           }
           else
           {
               Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
               Log.d("Account", "createAccount: Failure", task.exception)
           }
       }
    }

    private fun saveUserData() {
      userName = binding.username.text.toString()
        email = binding.emailadd.text.toString().trim()
        password = binding.passworrd.text.toString().trim()

        val user = UserModel(userName, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
         database.child("user").child(userId).setValue(user)



    }
}