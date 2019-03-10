package com.rendal.notebook.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rendal.notebook.data.entities.Note
import com.rendal.notebook.databinding.ItemNoteBinding
import android.text.method.TextKeyListener.clear
import androidx.recyclerview.widget.DiffUtil


class NotesAdapter(private var listener: OnAdapterListener, val notesList: ArrayList<Note> = ArrayList()) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(), ItemTouchHelperAdapter {
    override fun onItemDismiss(position: Int) {
        listener.onItemRemove(notesList.removeAt(position))
        notifyItemRemoved(position)
    }

    fun setNotesList(notes: List<Note>) {
        val productDiffUtilCallback = NoteDiffUtilCallback(notesList, notes)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

        notesList.clear()
        notesList.addAll(notes)

        productDiffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNoteBinding.inflate(inflater, parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = notesList.size


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notesList[position], listener)
    }

    class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note, listener: OnAdapterListener) {
            binding.note = note
            binding.root.setOnClickListener { listener.onItemClick(note) }
            binding.executePendingBindings()
        }
    }

    interface OnAdapterListener {
        fun onItemClick(note: Note)

        fun onItemRemove(note: Note)
    }
}