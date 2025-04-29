package com.example.kurirkilat.utils

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

object PhoneAuthUtils {

    private const val TAG = "PhoneAuthUtils"
    private lateinit var verificationId: String
    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null

    fun startPhoneNumberVerification(
        activity: Activity,
        phoneNumber: String,
        callbacks: PhoneAuthCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "Verification Completed")
                    callbacks.onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "Verification Failed: ${e.message}")
                    callbacks.onVerificationFailed(e)
                }

                // menyimpan verificationid untuk otp
                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d(TAG, "Code Sent: $id")
                    verificationId = id
                    resendingToken = token
                    callbacks.onCodeSent(id, token)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    // fungsi untuk verifikasi otp
    fun verifyOtpCode(
        otpCode: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otpCode)
        signInWithPhoneAuthCredential(credential, onSuccess, onFailure)
    }

    //fungsi kirim ulang otp (syntax mirip dengan fun startPhoneNumberVerification)
    fun resendVerificationCode(
        activity: Activity,
        phoneNumber: String,
        callbacks: PhoneAuthCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d(TAG, "Verification Completed")
                    callbacks.onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "Verification Failed: ${e.message}")
                    callbacks.onVerificationFailed(e)
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d(TAG, "Code Resent: $id")
                    verificationId = id
                    resendingToken = token
                    callbacks.onCodeSent(id, token)
                }
            })
            .setForceResendingToken(resendingToken!!)	//fungsi untuk kirim ulang otp, dengan operator "!!" artinya nilai gaboleh null. menggunakan setter untuk mengubah nilai resendingToken
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Sign in success")
                    onSuccess(task.result?.user)
                } else {
                    Log.e(TAG, "Sign in failed: ${task.exception?.message}")
                    onFailure(task.exception)
                }
            }
    }

    interface PhoneAuthCallbacks {
        fun onVerificationCompleted(credential: PhoneAuthCredential)
        fun onVerificationFailed(e: FirebaseException)
        fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
    }
}
