package com.example.project6

import androidx.recyclerview.widget.DiffUtil

class NoteDiffUtil : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) //update recycler, new/deleted notes.
            = (oldItem.noteId == newItem.noteId)

    override fun areContentsTheSame(oldItem: Note, newItem: Note) //no change to recycler.
            = (oldItem == newItem)
}