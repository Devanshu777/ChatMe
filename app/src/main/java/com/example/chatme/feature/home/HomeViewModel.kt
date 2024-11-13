package com.example.chatme.feature.home

import androidx.lifecycle.ViewModel
import com.example.chatme.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channel = _channels.asStateFlow()

    private val firebaseDatabase = Firebase.database
    val firebaseAuth = FirebaseAuth.getInstance()

    init {
        // Listen for real-time changes in the "channel" node
        firebaseDatabase.getReference("channel").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Channel>()
                snapshot.children.forEach { data ->
                    val channel = Channel(data.key!!, data.value.toString())
                    list.add(channel)
                }
                _channels.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    fun addChannel(name: String) {
        val key = firebaseDatabase.getReference("channel").push().key
        firebaseDatabase.getReference("channel").child(key!!).setValue(name)
        // No need to call getChannels() here, as the ValueEventListener will handle updates
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}