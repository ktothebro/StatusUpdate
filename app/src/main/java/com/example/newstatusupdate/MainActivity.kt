package com.example.newstatusupdate

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.example.newstatusupdate.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore

class MainActivity : AppCompatActivity() {

    // declare auth
    private lateinit var auth: FirebaseAuth
    // declare firstore database
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityMainBinding
    private lateinit var mEmail: String
    private lateinit var mPassword: String

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.resetButton.setOnClickListener {
            binding.etEmail.setText("")
            binding.etPassword.setText("")
        }

        binding.submitButton.setOnClickListener {
            mEmail = binding.etEmail.text.toString()
            mPassword = binding.etPassword.text.toString()
            signInWithEmailAndPassword()
        }

        // Initialize Firebase Auth and Firestore
        auth = Firebase.auth
        db = Firebase.firestore

    }

    private fun signInWithEmailAndPassword() {
        auth.signInWithEmailAndPassword(mEmail, mPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")

                    val user = auth.currentUser
                    getUserDescription()
                    updateUI(user)
                } else {
                    // If signin fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun getUserDescription() {
        val docRef = db.collection("users").document(mEmail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        // val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Navigate to MainActivity
        if(user == null){
            return
        }
        startActivity(Intent(this, StatusActivity::class.java))
        finish()
    }

//
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
//
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
//
//            // Google Sign In was successful, authenticate with Firebase
//            val account = task.getResult(ApiException::class.java)!!
//
//            // firebaseAuthWithGoogle(account.idToken!!)
//            Toast.makeText(this, "signIn Done" + account.id, Toast.LENGTH_LONG).show()
//
//            // Google Sign In failed, update UI appropriately
//            Log.d(TAG, "failed ----- kai b")
//            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show()
//
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
//}


//    val RC_SIGN_IN: Int = 1
//    lateinit var mGoogleSignInClient: GoogleSignInClient
//    lateinit var mGoogleSignInOptions: GoogleSignInOptions
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var firebaseAuth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        configureGoogleSignIn()
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        binding.googleButton.setOnClickListener {
//            signIn()
//        }
//
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_SIGN_IN) {
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account)
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    private fun signIn() {
//        val signInIntent: Intent = mGoogleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
//        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
//            if (it.isSuccessful) {
//
//             //   startActivity(StatusActivity.getLaunchIntent(this))
//            } else {
//                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            // startActivity(StatusActivity.getLaunchIntent(this))
//            finish()
//        }
//    }
//
//    private fun configureGoogleSignIn() {
//        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.web_client_id))
//            .requestEmail()
//            .build()
//        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
//    }
//
//    companion object {
//        fun getLaunchIntent(from: Context) = Intent(from, MainActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        }
//    }
}