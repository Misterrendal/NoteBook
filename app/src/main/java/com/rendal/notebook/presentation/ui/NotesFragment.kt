package com.rendal.notebook.presentation.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rendal.notebook.data.entities.Note

import com.rendal.notebook.presentation.viewmodels.NotesViewModel
import com.rendal.notebook.databinding.FragmentNotesBinding
import com.rendal.notebook.presentation.adapter.NotesAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import com.rendal.notebook.presentation.adapter.ItemTouchHelperCallback
import androidx.recyclerview.widget.DiffUtil
import com.rendal.notebook.presentation.adapter.NoteDiffUtilCallback


class NotesFragment : Fragment() {

    private var listener: OnFragmentChange? = null
    private var viewModel: NotesViewModel? = null
    private var adapter: NotesAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNotesBinding.inflate(inflater, container, false)
        adapter = NotesAdapter(object : NotesAdapter.OnAdapterListener {
            override fun onItemRemove(note: Note) {
                viewModel?.removeNote(note)
            }

            override fun onItemClick(note: Note) {
                listener?.onFragmentChange(newInstanceDetailFragment(note))
            }
        })
        if (savedInstanceState != null) {
            adapter?.setNotesList(savedInstanceState.getParcelableArrayList("extra_note")!!)
        }
        binding.notesRecyclerView.adapter = adapter
        val callback = ItemTouchHelperCallback(adapter!!)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.notesRecyclerView)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.fab.setOnClickListener { listener?.onFragmentChange(newInstanceDetailFragment(null)) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        viewModel?.notesLiveData?.observe(viewLifecycleOwner, Observer { adapter?.setNotesList(it) })
        viewModel?.messageLiveData?.observe(viewLifecycleOwner, Observer { showMessage(it, context) })
        if (adapter?.notesList?.isNullOrEmpty()!!)
            viewModel?.getNotes()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentChange) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentChange {
        fun onFragmentChange(fragment: Fragment)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("extra_note", adapter?.notesList)
    }
}

