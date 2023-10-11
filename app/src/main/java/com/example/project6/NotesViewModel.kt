package com.example.project6

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.project6.Note


class NotesViewModel(val dao: NoteDao) : ViewModel() {
    val notes = dao.getAllNotes()

    //setup a boolean, long pair so can separate editing notes from new notes.
    private val _navigateToNote = MutableLiveData<Pair<Boolean, Long?>>(Pair(false, null))
    val navigateToNote: LiveData<Pair<Boolean, Long?>>
        get() = _navigateToNote

    // navigate with null note so user can set their own title, description etc.
    fun addNote() {
        _navigateToNote.value = Pair(true, null)
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            val noteToDelete = Note(noteId = noteId) // find requested note w/ noteId
            dao.delete(noteToDelete)
        }
    }

    fun onNoteClicked(noteId: Long) { //navigate with requested noteId to edit
        _navigateToNote.value = Pair(true, noteId)
    }

    fun onNoteNavigated() { //ensure navigation doesnt run into trouble.
        _navigateToNote.value = Pair(false, null)
    }

    // logging function to print all notes to Logcat (debug)
    fun logAllNotes() {
        val allNotes = notes.value
        allNotes?.forEach { note ->
            Log.d("NotesViewModel", "Note Name: ${note.noteTitle}, Message: ${note.noteMessage}")
        }
    }
}