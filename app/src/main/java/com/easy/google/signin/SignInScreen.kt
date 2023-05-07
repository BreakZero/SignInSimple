package com.easy.google.signin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.easy.google.signin.models.SignInType

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: (SignInType) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.run {
            Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {

            }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                onSignInClick(SignInType.GOOGLE)
            }) {
                Text(text = "Sign In With Google")
            }
            Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
            Button(onClick = {
                onSignInClick(SignInType.GITHUB)
            }) {
                Text(text = "Sign In With Github")
            }
        }
    }
}