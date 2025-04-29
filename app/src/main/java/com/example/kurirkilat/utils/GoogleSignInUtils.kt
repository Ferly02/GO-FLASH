package com.example.kurirkilat.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.credentials.GetCredentialException
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.kurirkilat.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleSignInUtils {

    companion object {

        @SuppressLint("NewApi")
        fun doGoogleSignIn(
            context: Context,
            scope: CoroutineScope,
            launcher: ActivityResultLauncher<Intent>?,
            login: () -> Unit
        ) {
            val credentialManager = CredentialManager.create(context)

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()

            scope.launch {
                try {
                    val result = credentialManager.getCredential(context, request)
                    when (val credential = result.credential) {
                        is CustomCredential -> {
                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                val googleIdTokenCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)

                                val googleTokenId = googleIdTokenCredential.idToken

                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)

                                val user = Firebase.auth.signInWithCredential(authCredential).await().user
                                user?.let {
                                    if (!user.isAnonymous) {
                                        login.invoke()
                                    }
                                }
                            }
                        }

                        else -> {
                            // Handle other credential types if needed
                            Log.d("GoogleSignInUtils", "Credential type not handled: ${credential::class.java}")
                        }
                    }

                } catch (e: NoCredentialException) {
                    Log.e("GoogleSignInUtils", "No credentials found. Launching Add Account Intent")
                    launcher?.launch(getIntent())

                } catch (e: GetCredentialCancellationException) {
                    Log.e("GoogleSignInUtils", "User cancelled sign-in: ${e.localizedMessage}")
                    // Optional: Show toast atau feedback ke user
                    Toast.makeText(context, "Login dibatalkan", Toast.LENGTH_SHORT).show()

                } catch (e: GetCredentialException) {
                    Log.e("GoogleSignInUtils", "GetCredentialException: ${e.localizedMessage}")
                    Toast.makeText(context, "Gagal login: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Log.e("GoogleSignInUtils", "Unhandled error: ${e.localizedMessage}")
                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }

        private fun getCredentialOptions(context: Context): CredentialOption {
            val serverClientId = context.getString(R.string.default_web_client_id)
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(serverClientId)
                .build()
        }
    }
}
