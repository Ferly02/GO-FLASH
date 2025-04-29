package com.example.kurirkilat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var etOtpCode: EditText
    private lateinit var btnVerifyOtp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var verificationId: String
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        initViews()

        // Ambil data dari intent sebelumnya (SignUpActivity)
        verificationId = intent.getStringExtra("verificationId") ?: ""
        phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        if (verificationId.isEmpty()) {
            Toast.makeText(this, "Verification ID tidak ditemukan!", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnVerifyOtp.setOnClickListener {
            val code = etOtpCode.text.toString().trim()
            if (code.length < 6) {
                etOtpCode.error = "Masukkan kode OTP yang benar"
                return@setOnClickListener
            }

            verifyCode(code)
        }
    }

    private fun initViews() {
        etOtpCode = findViewById(R.id.etOtpCode)
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp)
        progressBar = findViewById(R.id.progressBarOtp)
    }

    private fun verifyCode(code: String) {
        progressBar.visibility = View.VISIBLE
        val credential = PhoneAuthProvider.getCredential(verificationId, code)

        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = task.result?.user
                    Toast.makeText(this, "Verifikasi berhasil!", Toast.LENGTH_SHORT).show()

                    // TODO: Simpan data user ke database kalau perlu

                    // Navigasi ke MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Kode OTP salah!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Verifikasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.e("VerifyOtp", "Error", task.exception)
                    }
                }
            }
    }
}
