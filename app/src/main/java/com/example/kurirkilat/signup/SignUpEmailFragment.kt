package com.example.kurirkilat.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kurirkilat.LoginActivity
import com.example.kurirkilat.R
import com.example.kurirkilat.utils.GoogleSignInUtils
import kotlinx.coroutines.CoroutineScope

class SignUpEmailFragment : Fragment() {

    // Untuk Google Sign-In dan coroutine Firebase
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var coroutineScope: CoroutineScope

    // View references
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnGoogleSignIn: Button
    private lateinit var tvLogin: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutContent: ScrollView
    private lateinit var btnPhoneSignUp: Button
    private lateinit var ivEmailCheck: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout fragment (BUKAN activity)
        return inflater.inflate(R.layout.fragment_email_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup coroutine dan semua view
        coroutineScope = viewLifecycleOwner.lifecycleScope
        initViews(view)
        setupListeners()
        setupGoogleSignIn()
        setupPhoneNavigation()
    }

    // Inisialisasi semua komponen UI dari layout
    private fun initViews(view: View) {
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnSignUp = view.findViewById(R.id.btnSignUp)
        btnGoogleSignIn = view.findViewById(R.id.btnGoogleSignIn)
        tvLogin = view.findViewById(R.id.tvLogin)
        progressBar = view.findViewById(R.id.progressLoading)
        layoutContent = view.findViewById(R.id.layoutContent)
        btnPhoneSignUp = view.findViewById(R.id.btnPhoneSignUp)
        ivEmailCheck = view.findViewById(R.id.ivEmailCheck)
    }

    // Listener tombol Sign Up Email
    private fun setupListeners() {
        btnSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            // Validasi input
            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmail.error = "Email tidak valid"
                }
                password != confirmPassword -> {
                    etConfirmPassword.error = "Password tidak sama"
                }
                else -> {
                    // Proses sign up (nanti sambung ke Firebase)
                    Toast.makeText(requireContext(), "Proses Sign Up...", Toast.LENGTH_SHORT).show()
                }
            }
        }
        etEmail.addTextChangedListener {
            val email = it.toString().trim()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ivEmailCheck.animate().alpha(1f).setDuration(150).withStartAction {
                    ivEmailCheck.visibility = View.VISIBLE
                }.start()
            } else {
                ivEmailCheck.animate().alpha(0f).setDuration(150).withEndAction {
                    ivEmailCheck.visibility = View.GONE
                }.start()
            }
        }
    }

    // Integrasi Google Sign-In
    private fun setupGoogleSignIn() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Toast.makeText(requireContext(), "Kembali dari Settings", Toast.LENGTH_SHORT).show()
        }

        btnGoogleSignIn.setOnClickListener {
            GoogleSignInUtils.doGoogleSignIn(
                context = requireActivity(),
                scope = coroutineScope,
                launcher = launcher,
                login = {
                    Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
            )
        }
        // 👉 Tambahkan listener ke text "Masuk"
        tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // optional, kalau mau close SignUpActivity
    }


    // Navigasi ke fragment pendaftaran via nomor HP
    private fun setupPhoneNavigation() {
        btnPhoneSignUp.setOnClickListener {
            (activity as? SignUpActivity)?.showPhoneFragment()
        }

    }

    // Fungsi redirect ke main (placeholder)
    private fun navigateToMain() {
        // nanti bisa diganti ke MainActivity
    }
}
