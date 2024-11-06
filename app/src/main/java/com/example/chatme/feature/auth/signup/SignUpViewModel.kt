package com.example.chatme.feature.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {


    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _signUpState.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        _signUpState.value = SignUpState.Loading

        // Perform sign-up logic here
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        it.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                        )

                        _signUpState.value = SignUpState.Success
                        return@addOnCompleteListener
                    }
                    _signUpState.value = SignUpState.Error
                } else {
                    _signUpState.value = SignUpState.Error
                }
            }
    }
}


sealed class SignUpState {
    object Success : SignUpState()
    object Error : SignUpState()
    object Loading : SignUpState()
    object Nothing : SignUpState()
}
