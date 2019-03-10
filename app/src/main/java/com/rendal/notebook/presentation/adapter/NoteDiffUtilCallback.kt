package com.rendal.notebook.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.rendal.notebook.data.entities.Note

class NoteDiffUtilCallback(private val oldList: List<Note>, private val newList: List<Note>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldList[oldItemPosition]
        val newNote = newList[newItemPosition]
        return oldNote.id === newNote.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = false
}