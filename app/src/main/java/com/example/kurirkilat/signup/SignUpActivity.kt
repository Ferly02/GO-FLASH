package com.example.kurirkilat.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kurirkilat.R

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Tampilkan fragment SignUpEmail sebagai tampilan default
        if (savedInstanceState == null) {
            showEmailFragment()
        }
    }

    /**
     * Navigasi kembali ke fragment SignUp menggunakan email.
     * Ditambahkan ke back stack untuk navigasi mundur jika perlu.(tapi dibawah tidak)
     */
    fun showEmailFragment() {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainerSignup)
        if (current !is SignUpEmailFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerSignup, SignUpEmailFragment())
                .commit()
        }
    }

    /**
     * Navigasi ke fragment SignUp menggunakan nomor telepon.
     * Ditambahkan ke back stack agar bisa kembali ke fragment sebelumnya.(tapi tidak dipakai)
     */
    fun showPhoneFragment() {
        val current = supportFragmentManager.findFragmentById(R.id.fragmentContainerSignup)
        if (current !is SignUpPhoneFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerSignup, SignUpPhoneFragment())
                .commit()
        }
    }
}
