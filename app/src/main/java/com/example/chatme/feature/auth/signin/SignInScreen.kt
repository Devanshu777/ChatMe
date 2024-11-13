package com.example.chatme.feature.auth.signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatme.R
import com.example.chatme.ui.theme.redHatFontFamily

@Composable
fun SignInScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val viewModel: SignInViewModel = hiltViewModel()

    val uiState = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {

        when (uiState.value) {
            is SignInState.Success -> {
                navController.navigate("home")
            }

            is SignInState.Error -> {
                Toast.makeText(context, "No user found", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    val isButtonEnabled by remember(email, password, uiState.value) {
        derivedStateOf {
            email.isNotBlank() && password.isNotBlank() && (uiState.value == SignInState.Nothing || uiState.value == SignInState.Error)
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll( rememberScrollState() ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Text(text = "ChatMe", fontSize = 26.sp, fontFamily = redHatFontFamily, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.size(22.dp))
            Text(text = "Welcome Back,", fontSize = 22.sp, fontFamily = redHatFontFamily, fontWeight = FontWeight.ExtraBold)
            Text(text = "Login to continue", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email address", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp, 0.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp, 0.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            Text(
                text = "Forget Password ?", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal,
                color = Color.Blue,
                modifier = Modifier
                    .clickable(onClick = { /*TODO*/ })
                    .align(Alignment.End)
                    .padding(end = 22.dp, top = 8.dp)
            )
            if (uiState.value == SignInState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.signIn(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp, 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    enabled = isButtonEnabled
                ) {
                    Text(text = "LOGIN", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal)
                }
            }
            Text(
                text = "Don't have an account ?  Sign Up", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.clickable(onClick = { navController.navigate("signup") })
            )
            Text(text = "Or", modifier = Modifier.padding(0.dp, 8.dp), fontFamily = redHatFontFamily, fontWeight = FontWeight.Bold)
            Text(
                text = "Login using phone number", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.clickable(onClick = { /*TODO*/ })
            )
        }
    }
}