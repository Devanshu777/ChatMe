package com.example.chatme.feature.auth.signin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {


    private val _signInState = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state = _signInState.asStateFlow()

    fun signIn(email: String, password: String) {
        _signInState.value = SignInState.Loading

        // Perform sign-in logic here
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        _signInState.value = SignInState.Success
                        return@addOnCompleteListener
                    }
                    _signInState.value = SignInState.Error
                } else {
                    _signInState.value = SignInState.Error
                }
            }
    }
}


sealed class SignInState {
    object Success : SignInState()
    object Error : SignInState()
    object Loading : SignInState()
    object Nothing : SignInState()
}
