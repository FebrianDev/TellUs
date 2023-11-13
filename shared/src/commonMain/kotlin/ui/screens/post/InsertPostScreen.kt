package ui.screens.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.post.model.PostRequest
import data.post.state.PostState
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.components.BtnRounded
import ui.components.TextBodyMedium
import ui.components.TextSubtitleMedium
import ui.components.TitleHeader
import ui.screens.post.items.ItemTag
import ui.themes.bgColor
import ui.themes.colorPrimary
import ui.themes.colorSecondary
import utils.getUid

class InsertPostScreen : Screen {
    @OptIn(ExperimentalResourceApi::class, ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {

        val uid = getUid()

        val postViewModel = getViewModel(Unit, viewModelFactory { PostViewModel() })

        val scaffoldState = remember { SnackbarHostState() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()

        var textMessage by remember { mutableStateOf(TextFieldValue("")) }
        var isPrivate by remember { mutableStateOf(false) }

        var selectedItem by remember { mutableStateOf(-1) }
        var selectedTag by remember { mutableStateOf("") }

        val navigator = LocalNavigator.currentOrThrow

        val listTag =
            arrayListOf("Random", "Social", "Study", "Politic", "Technology", "Gaming", "Beauty")

        Scaffold(
            containerColor = bgColor,
            snackbarHost = {
                SnackbarHost(hostState = scaffoldState)
            }
        ) {
            Column {
                TitleHeader("Create New Post", navigator)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource("drawable/icon_app.png"),
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                    Text("${textMessage.text.length}/320")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = textMessage,
                    onValueChange = {
                        if (textMessage.text.length < 320)
                            textMessage = it
                    },
                    placeholder = { Text(text = "Tell your story") },
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth().heightIn(180.dp)
                        .background(bgColor),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = bgColor,
                        unfocusedContainerColor = bgColor,
                        disabledContainerColor = bgColor,
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(
                            handleColor = colorPrimary,
                            backgroundColor = colorSecondary
                        )
                    ),
                    minLines = 4,
                    maxLines = 8
                )
                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                ) {
                    repeat(listTag.size) {
                        ItemTag(listTag[it], isHighlight = selectedItem == it) {
                            selectedItem = it
                            selectedTag = listTag[it]
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = isPrivate,
                            onCheckedChange = {
                                isPrivate = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorPrimary,
                                checkedTrackColor = colorSecondary,
                                uncheckedThumbColor = colorPrimary,
                                uncheckedTrackColor = bgColor
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // Display the switch state
                        TextBodyMedium(
                            text = "Private Post",
                            modifier = Modifier.wrapContentWidth()
                        )

                    }

                    BtnRounded(
                        "Share", modifier = Modifier
                            .wrapContentSize(),
                        enable = selectedTag.isNotEmpty() && textMessage.text.length >= 21
                    ) {

                        if (selectedTag.isEmpty()) return@BtnRounded
                        if (textMessage.text.length < 21) return@BtnRounded

                        postViewModel.insertPost(
                            PostRequest(
                                id_user = uid,
                                is_private = isPrivate,
                                message = textMessage.text,
                                tag = selectedTag,
                                token = "klfngklf"
                            )
                        )
                    }
                }
            }
        }
        var isDialogOpen by remember { mutableStateOf(true) }

        //handle network
        postViewModel.insertPostState.collectAsState().value.onSuccess {
            when (it) {
                is PostState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(
                            color = colorPrimary
                        )
                    }
                }

                is PostState.Error -> {
                    coroutineScope.launch {
                        scaffoldState.showSnackbar(
                            message = "Something was wrong!",
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is PostState.Success -> {
                    if (isDialogOpen) {
                        AlertDialog(
                            containerColor = bgColor,
                            onDismissRequest = { },
                            confirmButton = {

                            },

                            title = {
                                TextSubtitleMedium("Success Insert Post")
                            },
                            text = {
                                Column {
                                    BtnRounded("Finish") {
                                        isDialogOpen = false
                                        navigator.push(HomeScreen())
                                    }
                                }
                            }
                        )
                    }
                }

                else -> {

                }
            }
        }.onFailure {
            coroutineScope.launch {
                scaffoldState.showSnackbar(
                    message = "Internal server error",
                    duration = SnackbarDuration.Short
                )
            }
        }

    }

}