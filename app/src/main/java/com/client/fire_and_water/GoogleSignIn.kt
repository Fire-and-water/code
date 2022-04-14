package com.client.fire_and_water

import android.widget.Toast

import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseUser


//class GoogleSignIn {
//    //Change UI according to user data.
//    fun updateUI(account: FirebaseUser?) {
//        if (account != null) {
//            Toast.makeText(requireContext(), "You Signed In successfully", Toast.LENGTH_LONG).show()
////            startActivity(Intent(this, AnotherActivity::class.java))
//        } else {
//            Toast.makeText(requireContext(), "You Didnt signed in", Toast.LENGTH_LONG).show()
//        }
//    }
//}