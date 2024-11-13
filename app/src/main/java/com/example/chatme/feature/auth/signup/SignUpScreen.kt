//package com.example.chatme.feature.auth.signup
//
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.chatme.R
//import com.example.chatme.feature.auth.signin.SignInScreen
//import com.example.chatme.feature.auth.signin.SignInState
//
//@Composable
//fun SignUpScreen(navController: NavController) {
//    var email by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//
//    val viewModel: SignUpViewModel = hiltViewModel()
//    val uiState = viewModel.state.collectAsState()
//
//    val context = LocalContext.current
//    LaunchedEffect(key1 = uiState.value) {
//
//        when (uiState.value) {
//            is SignUpState.Success -> {
//                navController.navigate("home")
//            }
//
//            is SignUpState.Error -> {
//                Toast.makeText(context, "No user found", Toast.LENGTH_SHORT).show()
//            }
//
//            else -> {}
//        }
//    }
//
//    Scaffold(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(it)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = null,
//                modifier = Modifier
//                    .size(90.dp)
//                    .background(Color.White)
//            )
//            OutlinedTextField(onValueChange = { name = it },
//                value = name,
//                modifier = Modifier.fillMaxWidth(),
//                label = { Text(text = "Full Name") })
//
//            OutlinedTextField(onValueChange = { email = it },
//                value = email,
//                modifier = Modifier.fillMaxWidth(),
//                label = { Text(text = "Email") })
//
//            OutlinedTextField(
//                value = password,
//                modifier = Modifier.fillMaxWidth(),
//                onValueChange = { password = it },
//                label = { Text(text = "Password") },
//                visualTransformation = PasswordVisualTransformation()
//            )
//
//            OutlinedTextField(
//                value = confirmPassword,
//                modifier = Modifier.fillMaxWidth(),
//                onValueChange = { confirmPassword = it },
//                label = { Text(text = "Confirm Password") },
//                visualTransformation = PasswordVisualTransformation(),
//                isError = password != confirmPassword && confirmPassword.isNotEmpty()
//            )
//
//            Spacer(modifier = Modifier.size(14.dp))
//
//            if (uiState.value == SignUpState.Loading) {
//                CircularProgressIndicator()
//            } else {
//
//                Button(
//                    onClick = { viewModel.signUp(name, email, password) },
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
//                ) {
//                    Text(text = "Sign Up")
//                }
//
//                TextButton(onClick = { navController.popBackStack() }) {
//                    Text(text = "Already have an account ?  Sign In")
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewSignUpScreen() {
//    SignUpScreen(navController = rememberNavController())
//}

package com.example.chatme.feature.auth.signup

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
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val viewModel: SignUpViewModel = hiltViewModel()

    val uiState = viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {

        when (uiState.value) {
            is SignUpState.Success -> {
                navController.navigate("home")
            }

            is SignUpState.Error -> {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    val isButtonEnabled by remember(email, password, uiState.value) {
        derivedStateOf {
            name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword && (uiState.value == SignUpState.Nothing || uiState.value == SignUpState.Error)
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll( rememberScrollState()),
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
            Text(text = "Create Account", fontSize = 22.sp, fontFamily = redHatFontFamily, fontWeight = FontWeight.ExtraBold)
            Text(text = "SignUp here", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal)
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Full Name", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp, 0.dp)
            )
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
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Confirm Password", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp, 0.dp),
                visualTransformation = PasswordVisualTransformation()
            )
            if (uiState.value == SignUpState.Loading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.signUp(name, email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp, 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    enabled = isButtonEnabled
                ) {
                    Text(text = "SIGNUP", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal )
                }
            }
            Text(
                text = "Already have an account ?  Sign In", fontFamily = redHatFontFamily, fontWeight = FontWeight.Normal,
                color = Color.Gray,
                modifier = Modifier.clickable(onClick = { navController.popBackStack() })
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