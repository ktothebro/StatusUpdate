package com.example.newstatusupdate

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.newstatusupdate.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : AppCompatActivity() {

//    // [START declare_auth]
//    private lateinit var auth: FirebaseAuth
//    // [END declare_auth]
//
//    val RC_SIGN_IN: Int = 1
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    lateinit var mGoogleSignInOptions: GoogleSignInOptions
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        binding.btnSignIn.setOnClickListener {
//            signIn()
//        }
//        // [START config_signin]
//        // Configure Google Sign In
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.web_client_id))
//            .requestEmail()
//            .build()
//
////        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        // [END config_signin]
//
//        configureGoogleSignIn()
//
//        // [START initialize_auth]
//        // Initialize Firebase Auth
//        auth = Firebase.auth
//        // [END initialize_auth]
//    }
//
//    // [START on_start_check_user]
//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
//    }
//    // [END on_start_check_user]
//
//    // [START onactivityresult]
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////    }
//    // [END onactivityresult]
//
//    // [START auth_with_google]
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        Toast.makeText(this, "Func Tried", Toast.LENGTH_LONG).show()
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        Toast.makeText(this, "Func Tried", Toast.LENGTH_LONG).show()
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithCredential:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_LONG).show()
//                    updateUI(null)
//                }
//            }
//    }
//    // [END auth_with_google]
//
//    // [START signin]
//    private fun signIn() {
//        val signInIntent = mGoogleSignInClient.signInIntent
//
//
//
//        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
//
//        try {
//            // Google Sign In was successful, authenticate with Firebase
//            val account = task.getResult(ApiException::class.java)!!
//
//            // firebaseAuthWithGoogle(account.idToken!!)
//            Toast.makeText(this, "signIn Done" + account.id, Toast.LENGTH_LONG).show()
//        } catch (e: ApiException) {
//            // Google Sign In failed, update UI appropriately
//            Log.d(TAG, "failed ----- kai b")
//            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
//        }
//        Toast.makeText(this, "Done with func", Toast.LENGTH_LONG).show()
//    }
//    // [END signin]
//
//    private fun updateUI(user: FirebaseUser?) {
//        // Navigate to MainActivity
//        if(user == null){
//            return
//        }
//        startActivity(Intent(this, StatusActivity::class.java))
//        finish()
//    }
//
//    private fun configureGoogleSignIn() {
//         mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.web_client_id))
//            .requestEmail()
//            .build()
//        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
//    }
//
//    companion object {
//        private const val TAG = "GoogleActivity"
//        private const val RC_SIGN_IN = 9001
//    }
/*
    private companion object {
        private const val TAG = "LoginActivity"
        private const val RC_GOOGLE_SIGN_IN = 4926
    }

    private lateinit var binding: ActivityMainBinding
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    private lateinit var btnSignIn: SignInButton
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSignIn.setOnClickListener {
            signIn()
        }


        btnSignIn = binding.btnSignIn
        FirebaseApp.initializeApp(this)
        oneTapClient = Identity.getSignInClient(this)

        configureGoogleSignIn()

        Log.d(TAG, "HELLO1.")
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        binding.btnSignIn.setOnClickListener {
            signIn()
        }
        auth = Firebase.auth
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user = auth.currentUser
                                        if (user != null) {
                                            Firebase.auth.updateCurrentUser(user)
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                                    }
                                }
                        }
                        else -> {

                        }
                    }
                } catch (e: ApiException) {
                    // ...
                }
            }
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    } */
}

