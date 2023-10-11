package com.example.project6

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EditNoteViewModel(noteId: Long, val dao: NoteDao) : ViewModel() {
    val note: LiveData<Note> = dao.get(noteId)

    private val _navigateToList = MutableLiveData<Boolean>(false)
    val navigateToList: LiveData<Boolean>
        get() = _navigateToList

    fun saveNote(title: String, description: String) {
        viewModelScope.launch {
            Log.d("NotesViewModel", "CALLED!") //debug

            val currentNote = note.value

            if (currentNote != null) { //editing
                // edit/update the note's title and description
                currentNote.noteTitle = title
                currentNote.noteMessage = description
                dao.update(currentNote)

            } else { //new note
                // create a new note with the provided title and description
                val newNote = Note(noteTitle = title, noteMessage = description)
                dao.insert(newNote)
                Log.d("NotesViewModel", "Note Name: ${newNote.noteTitle}, Message: ${newNote.noteMessage}")
            }

            Log.d("NotesViewModel", "NAVIGATING") //debug

            _navigateToList.value = true // navigate back to menu
        }
    }

    fun onNavigatedToList() {
        _navigateToList.value = false
    }
}