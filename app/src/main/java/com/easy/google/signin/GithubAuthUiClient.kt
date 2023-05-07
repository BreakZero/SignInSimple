package com.easy.google.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.easy.google.signin.models.SignInResult
import com.easy.google.signin.models.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.util.concurrent.CancellationException

class GithubAuthUiClient(
    private val activity: Activity
) {
    private val auth = Firebase.auth
    private val githubAuthProvider = OAuthProvider.newBuilder("github.com").build()

    suspend fun signIn(): SignInResult {
        val pendingResultTask = auth.pendingAuthResult
        return if (pendingResultTask != null) {
            try {
                val user = pendingResultTask.await().user
                SignInResult(
                    data = user?.run {
                        UserData(
                            userId = uid,
                            username = this.displayName,
                            profilePictureUrl = this.photoUrl.toString()
                        )
                    },
                    errorMessage = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                SignInResult(
                    data = null,
                    errorMessage = e.message
                )
            }
        } else {
            try {
                val user = auth.startActivityForSignInWithProvider(activity, githubAuthProvider).await().user
                SignInResult(
                    data = user?.run {
                        UserData(
                            userId = uid,
                            username = this.displayName,
                            profilePictureUrl = this.photoUrl.toString()
                        )
                    },
                    errorMessage = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                SignInResult(
                    data = null,
                    errorMessage = e.message
                )
            }
        }

    }

    fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = this.uid,
            username = this.displayName,
            profilePictureUrl = this.photoUrl.toString()
        )
    }
}