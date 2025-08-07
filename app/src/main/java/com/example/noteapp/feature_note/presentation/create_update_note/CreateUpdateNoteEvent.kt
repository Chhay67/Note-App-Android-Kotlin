package com.example.noteapp.feature_note.presentation.create_update_note

import androidx.compose.ui.focus.FocusState

sealed class CreateUpdateNoteEvent{
    data class EnteredTitle(val value: String) : CreateUpdateNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : CreateUpdateNoteEvent()
    data class EnteredContent(val value: String) : CreateUpdateNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState) : CreateUpdateNoteEvent()
    data class ChangeColor(val color: Int) : CreateUpdateNoteEvent()
    object SaveNote : CreateUpdateNoteEvent()

}