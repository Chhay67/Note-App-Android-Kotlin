package com.example.noteapp.feature_note.presentation.create_update_note

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true,
    val singleLine: Boolean = true,
    val maxLines: Int = Int.MAX_VALUE
)
