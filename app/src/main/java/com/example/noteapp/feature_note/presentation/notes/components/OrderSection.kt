package com.example.noteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noteapp.feature_note.domain.utils.NoteOrder
import com.example.noteapp.feature_note.domain.utils.OrderType

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DefaultRadioButton(
                title = "Title",
                onSelected = noteOrder is NoteOrder.Title,
                onCheck = {
                    onOrderChange(NoteOrder.Title(noteOrder.orderType))
                },

                )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                title = "Date",
                onSelected = noteOrder is NoteOrder.Date,
                onCheck = {
                    onOrderChange(NoteOrder.Date(noteOrder.orderType))
                },

                )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                title = "Color",
                onSelected = noteOrder is NoteOrder.Color,
                onCheck = {
                    onOrderChange(NoteOrder.Color(noteOrder.orderType))
                },

                )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                title = "Ascending",
                onSelected = noteOrder.orderType is OrderType.Ascending,
                onCheck = {
                    onOrderChange(noteOrder.copy(OrderType.Ascending))
                },

                )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                title = "Descending",
                onSelected = noteOrder.orderType is OrderType.Descending,
                onCheck = {
                    onOrderChange(noteOrder.copy(OrderType.Descending))
                },

                )
        }
    }
}

