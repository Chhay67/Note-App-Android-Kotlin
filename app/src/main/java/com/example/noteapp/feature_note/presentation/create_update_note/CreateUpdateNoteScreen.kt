package com.example.noteapp.feature_note.presentation.create_update_note

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.presentation.create_update_note.components.TransparentHintTextField
import kotlinx.coroutines.launch


@Composable
fun CreateUpdateNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: CreateUpdateNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val snackBarHostState = remember { SnackbarHostState() }
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is CreateUpdateNoteViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                is CreateUpdateNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                contentColor = MaterialTheme.colorScheme.primary,

                onClick = {
                    viewModel.onEvent(CreateUpdateNoteEvent.SaveNote)

                }) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Note",
                )
            }
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp),

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Note.noteColor.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape) // Clip first to enforce circular shape
                            .background(Color(colorInt)) // Apply background within the clipped shape
                            .border(
                                width = 3.dp,
                                color = if (colorInt == viewModel.noteColor.value) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .shadow(10.dp, CircleShape) // Optional: use with shape for better circular shadow
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(durationMillis = 500)
                                    )
                                }
                                viewModel.onEvent(CreateUpdateNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(CreateUpdateNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(
                        CreateUpdateNoteEvent.ChangeTitleFocus(
                            it
                        )
                    )
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                modifier = Modifier.fillMaxSize(),
                onValueChange = {
                    viewModel.onEvent(CreateUpdateNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(
                        CreateUpdateNoteEvent.ChangeContentFocus(
                            it
                        )
                    )
                },
                isHintVisible = contentState.isHintVisible,
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }


    }

}


