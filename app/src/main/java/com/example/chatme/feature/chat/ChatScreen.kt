package com.example.chatme.feature.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatme.model.Message
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.example.chatme.R
import com.example.chatme.ui.theme.redHatFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, channelId: String) {
    // UI for the chat screen
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "ChatMe", fontSize = 26.sp, fontFamily = redHatFontFamily, fontWeight = FontWeight.ExtraBold)},
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White  // or any color you prefer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            Image(
                painter = painterResource(id = R.drawable.chat_bg),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            val viewModel: ChatViewModel = hiltViewModel()
            LaunchedEffect(key1 = true) {
                viewModel.listenForMessages(channelId)
            }
            val messages = viewModel.messages.collectAsState()
            ChatMessages(
                messages = messages.value,
                onSendMessage = { message ->
                    viewModel.sendMessage(channelId, message)
                }
            )
        }
    }
}


@Composable
fun ChatMessages(
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
) {
    val hideKeyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    val msg = remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,       // Align items to the bottom so that the chat message to appear at the bottom
            contentPadding = PaddingValues(bottom = 70.dp), // Add bottom padding
            state = listState
        ) {
            items(messages.size) { message ->
                ChatBubble(message = messages[message])
            }
        }
        LaunchedEffect(messages) {            //this is to scroll to new chat
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.lastIndex)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White), verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = {
                msg.value = ""
            }) {
                Image(
                    painter = painterResource(id = R.drawable.attach),
                    contentDescription = "attach"
                )
            }

            TextField(
                value = msg.value,
                onValueChange = { msg.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(text = "Type a message") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    hideKeyboardController?.hide()
                }),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                )
            )
            IconButton(onClick = {
                onSendMessage(msg.value)
                msg.value = ""
            }) {
                Image(painter = painterResource(id = R.drawable.send), contentDescription = "send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val isCurrentUser = message.senderId == Firebase.auth.currentUser?.uid
    val bubbleColor = if (isCurrentUser) {
        Color.Green
    } else {
        Color.LightGray
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)

    ) {
        val alignment = if (!isCurrentUser) Alignment.CenterStart else Alignment.CenterEnd
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCurrentUser) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    // To Display username below avatar
                    Text(
                        text = message.senderName.trim(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = redHatFontFamily
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = message.message.trim(),
                color = Color.White,
                fontFamily = redHatFontFamily,
                modifier = Modifier
                    .background(color = bubbleColor, shape = RoundedCornerShape(22.dp))
                    .padding(8.dp)
            )
        }
    }
}