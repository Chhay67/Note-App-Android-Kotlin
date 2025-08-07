package com.example.noteapp.feature_note.presentation.create_update_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateUpdateNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    saveStateHandle: SavedStateHandle
    ) : ViewModel() {

    init {
        saveStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.intValue = note.color
                    }
                }
            }
        }
    }

    private var currentNoteId: Int? = null
    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title",
            singleLine = true,
            maxLines = 1
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle
    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter content",
            singleLine = false,
            maxLines = Int.MAX_VALUE
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColor.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }

    fun onEvent(event: CreateUpdateNoteEvent) {
        when (event) {
            is CreateUpdateNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }

            is CreateUpdateNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }

            is CreateUpdateNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }

            is CreateUpdateNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }

            is CreateUpdateNoteEvent.ChangeColor -> {
                _noteColor.intValue = event.color
            }

            CreateUpdateNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)

                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )

                    }

                }
            }
        }

    }
}