package org.david.notes.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel = viewModel { SignUpViewModel() }

    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword = viewModel.confirmPassword.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up Screen", fontSize = 22.sp)
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = {
                viewModel.onEmailChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Email")
            },
            label = {
                Text("Email")
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = {
                viewModel.onPasswordChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Password")
            },
            label = {
                Text("Password")
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = {
                viewModel.onConfirmPasswordChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Confirm Password")
            },
            label = {
                Text("Confirm Password")
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        TextButton(onClick = {
            navController.navigate("signin")
        }) {
            Text("Already have an account? Login")
        }

        Spacer(modifier = Modifier.size(16.dp))


        Button(onClick = { viewModel.onSignUpClick() }, modifier = Modifier.fillMaxWidth()) {
            Text("Sign Up")
        }
    }

}
