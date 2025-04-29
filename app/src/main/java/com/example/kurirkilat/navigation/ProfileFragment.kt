package com.example.kurirkilat.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.kurirkilat.LoginActivity
import com.example.kurirkilat.R
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment: Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up logout button
        setupLogoutButton(view)

        return view

    }

    // Setup tombol logout
    private fun setupLogoutButton(view: View) {
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener { logout() }
    }


    private fun navigateToLogin() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)  // Use requireActivity() to get the activity context
        startActivity(intent)
        requireActivity().finish() // Finish the activity to prevent going back
    }


    // Fungsi logout
    private fun logout() {
        firebaseAuth.signOut()
        navigateToLogin()
    }
}