package com.example.kurirkilat

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kurirkilat.signup.SignUpActivity
import com.example.kurirkilat.utils.GoogleSignInUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope

class  LoginActivity : AppCompatActivity() {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var coroutineScope: CoroutineScope

    // View Binding Manual (kalau nggak pake ViewBinding/Databinding)
    private lateinit var etEmail: EditText
    private lateinit var icCheckEmail: ImageView
    private lateinit var btnGoogleSignIn: Button
    private lateinit var progressLoading: ProgressBar
    private lateinit var layoutContent: RelativeLayout
    private lateinit var tvSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()         // Inisialisasi semua View
        setupGoogleSignIn() // Setup Google Sign In
        setupEmailWatcher() // Tambah listener di kolom email
        setupNavigation() // navigasi ke signupactivity

        coroutineScope = lifecycleScope
    }

    override fun onStart() {
        super.onStart()

        Toast.makeText(this, "Masuk LoginActivity", Toast.LENGTH_SHORT).show()
        showLoading(true)

        val user = Firebase.auth.currentUser
        if (user != null && !user.isAnonymous) {
            navigateToMain()
        } else {
            showLoading(false)
        }
    }

    // 🔹 Inisialisasi semua View biar rapih
    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        icCheckEmail = findViewById(R.id.ivEmailCheck)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
        progressLoading = findViewById(R.id.progressLoading)
        layoutContent = findViewById(R.id.layoutContent)
        tvSignUp = findViewById(R.id.tvSignUp)
    }

    // 🔹 Setup Google Sign-In Button
    private fun setupGoogleSignIn() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Toast.makeText(this, "Kembali dari Settings", Toast.LENGTH_SHORT).show()
        }

        btnGoogleSignIn.setOnClickListener {
            GoogleSignInUtils.doGoogleSignIn(
                context = this,
                scope = coroutineScope,
                launcher = launcher,
                login = {
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
            )
        }
    }

    // 🔹 Tambahkan TextWatcher untuk email field
    private fun setupEmailWatcher() {
        etEmail.addTextChangedListener(emailTextWatcher)
    }

    // 🔹 Listener buat validasi email
    private val emailTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val emailInput = s.toString().trim()
            if (isValidEmail(emailInput)) {
                icCheckEmail.visibility = View.VISIBLE
            } else {
                icCheckEmail.visibility = View.GONE
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // ga perlu apa-apa disini
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // ga perlu juga, semua di afterTextChanged
        }
    }


    // 🔹 Fungsi validasi email
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // 🔹 Navigasi ke MainActivity
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 🔹 Show / Hide Loading (ProgressBar)
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressLoading.visibility = View.VISIBLE
            layoutContent.alpha = 0.5f
            layoutContent.isEnabled = false
        } else {
            progressLoading.visibility = View.GONE
            layoutContent.alpha = 1f
            layoutContent.isEnabled = true
        }
    }

    // 🔹 Animasi Fade In
    private fun fadeIn(view: View, duration: Long = 300L) {
        view.apply {
            if (visibility == View.VISIBLE) return // Biar gak trigger ulang
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(duration)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    // 🔹 Animasi Fade Out
    private fun fadeOut(view: View, duration: Long = 300L) {
        view.apply {
            if (visibility == View.GONE) return // Biar gak trigger ulang
            animate()
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction {
                    visibility = View.GONE
                }
                .start()
        }
    }

    // 🔹 Toast helper
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupNavigation() {
        tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
