package com.example.noteapp.feature_note.presentation.util

sealed class Screen(val route: String) {

    data object NoteScreen : Screen("note_screen")
    data object CreateUpdateNoteScreen : Screen("create_update_note_screen")
}