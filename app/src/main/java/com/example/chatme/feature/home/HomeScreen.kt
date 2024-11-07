package com.example.chatme.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Your home screen UI
    val viewModel = hiltViewModel<HomeViewModel>()
    val channels = viewModel.channel.collectAsState()
    val addChannel = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        floatingActionButton = {
            Box(modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.Black)
                .clickable {
                    addChannel.value = true
                }) {
                Text(
                    text = "+ New Chat",
                    modifier = Modifier
                        .padding(16.dp, 8.dp),
                    color = Color.White
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    Text(
                        text = "ChatMe",
                        color = Color.Black,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Black),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                item {
                    TextField(value = "",
                        onValueChange = {},
                        placeholder = { Text(text = "Search...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(
                                RoundedCornerShape(40.dp)
                            ),
                        textStyle = TextStyle(color = Color.LightGray),
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Gray,
                            unfocusedTextColor = Color.Gray,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            focusedIndicatorColor = Color.Gray
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search, contentDescription = null
                            )
                        })
                }

                items(channels.value) { channel ->
                    Column {
                        ChannelItem(channel.name) {
                            navController.navigate("chat/${channel.id}")
                        }
                    }
                }
            }
        }
    }

    if (addChannel.value) {
        ModalBottomSheet(onDismissRequest = { addChannel.value = false }, sheetState = sheetState) {
            AddChannelDialog(onAddChannel = {
                viewModel.addChannel(it)
                addChannel.value = false
            })
        }
    }
}

@Composable
fun ChannelItem(channelName: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .shadow(1.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White)
            .clickable {
                onClick()
            },

    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Black)

            ) {
                Text(
                    text = channelName[0].uppercase(),
                    color = Color.White,
                    style = TextStyle(fontSize = 35.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            Text(text = channelName, modifier = Modifier.padding(8.dp), color = Color.Black)
        }
    }
}

@Composable
fun AddChannelDialog(onAddChannel: (String) -> Unit) {
    val channelName = remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "+ New Chat")

        Spacer(modifier = Modifier.padding(8.dp))

        //done
        TextField(value = channelName.value, onValueChange = {
            channelName.value = it
        }, label = { Text(text = "Chat Name", color = Color.DarkGray) }, singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
            ), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        //done
        Button(
            onClick = { onAddChannel(channelName.value) }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Add")
        }

    }
}