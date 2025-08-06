package com.example.noteapp.feature_note.presentation.create_update_note.components

import androidx.lifecycle.ViewModel
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CreateUpdateNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,


    ) : ViewModel() {

}