package com.example.kurirkilat.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.kurirkilat.MainActivity
import com.example.kurirkilat.R
import com.example.kurirkilat.VerifyOtpActivity
import com.example.kurirkilat.utils.PhoneAuthUtils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpPhoneFragment : Fragment() {

    // View references
    private lateinit var etPhone: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSendOtp: Button
    private lateinit var tvLoginEmail: TextView

    /**
     * Inflate layout XML untuk fragment ini
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_phone_signup, container, false)

    /**
     * Dipanggil setelah layout di-inflate dan view tersedia
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setListeners()
    }

    /**
     * Inisialisasi komponen UI dengan findViewById
     */
    private fun bindViews(view: View) {
        etPhone = view.findViewById(R.id.etPhone)
        progressBar = view.findViewById(R.id.progressLoading)
        btnSendOtp = view.findViewById(R.id.btnSendOtp)
        tvLoginEmail = view.findViewById(R.id.tvLoginEmail)
    }

    /**
     * Menambahkan listener ke tombol dan elemen interaktif lainnya
     */
    private fun setListeners() {
        btnSendOtp.setOnClickListener { validatePhoneAndSendOtp() }
        tvLoginEmail.setOnClickListener { navigateToEmailSignUp() }
    }

    /**
     * Validasi input nomor telepon dan mulai proses OTP jika valid
     */
    private fun validatePhoneAndSendOtp() {
        val rawPhone = etPhone.text.toString().trim()

        if (!isValidPhoneNumber(rawPhone)) {
            etPhone.error = "Nomor telepon tidak valid"
            return
        }

        val formattedPhone = "+62${rawPhone.substring(1)}"
        startPhoneVerification(formattedPhone)
    }

    /**
     * Verifikasi format nomor telepon dengan regex dan awalan "08"
     */
    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^[0-9]{10,13}$")) && phone.startsWith("08")
    }

    /**
     * Memulai proses pengiriman OTP ke nomor telepon menggunakan Firebase
     */
    private fun startPhoneVerification(phoneNumber: String) {
        showLoading(true)

        PhoneAuthUtils.startPhoneNumberVerification(
            requireActivity(),
            phoneNumber,
            object : PhoneAuthUtils.PhoneAuthCallbacks {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    handleVerificationSuccess()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    handleVerificationFailure(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    navigateToOtpScreen(verificationId, phoneNumber)
                }
            }
        )
    }

    /**
     * Callback jika verifikasi berhasil secara otomatis
     */
    private fun handleVerificationSuccess() {
        showLoading(false)
        showToast("Verifikasi berhasil")
        navigateToMain()
    }

    /**
     * Callback jika verifikasi gagal karena error jaringan atau format
     */
    private fun handleVerificationFailure(exception: FirebaseException) {
        showLoading(false)
        showToast("Verifikasi gagal: ${exception.message}")
        Log.e("PhoneAuth", "Error: ${exception.message}", exception)
    }

    /**
     * Navigasi ke layar verifikasi OTP dengan membawa verificationId dan phoneNumber
     */
    private fun navigateToOtpScreen(verificationId: String, phoneNumber: String) {
        showLoading(false)
        showToast("Kode OTP dikirim ke $phoneNumber")

        val intent = Intent(requireContext(), VerifyOtpActivity::class.java).apply {
            putExtra("verificationId", verificationId)
            putExtra("phoneNumber", phoneNumber)
        }
        startActivity(intent)
    }

    /**
     * Navigasi ke halaman utama (main activity) setelah login berhasil
     */
    private fun navigateToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    /**
     * Navigasi kembali ke halaman Sign Up Email
     */
    private fun navigateToEmailSignUp() {
        (activity as? SignUpActivity)?.showEmailFragment()
    }

    /**
     * Menampilkan loading spinner dan men-disable tombol sementara
     */
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnSendOtp.isEnabled = !isLoading

        if (isLoading) {
            lifecycleScope.launch {
                delay(3000) // Anti-spam: tombol dikunci selama 3 detik
                btnSendOtp.isEnabled = true
            }
        }
    }

    /**
     * Utility untuk menampilkan toast message
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
