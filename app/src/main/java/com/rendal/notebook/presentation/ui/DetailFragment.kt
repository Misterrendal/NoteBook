package com.rendal.notebook.presentation.ui

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rendal.notebook.R
import com.rendal.notebook.data.entities.Note
import com.rendal.notebook.databinding.FragmentDetailBinding
import com.rendal.notebook.presentation.viewmodels.DetailViewModel

private const val EXTRA_NOTE = "extra_note"

class DetailFragment : Fragment() {

    private var note: Note? = null
    private lateinit var viewModel: DetailViewModel
    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { note = it.getParcelable(EXTRA_NOTE) }
        if (note == null)
            note = Note("", "")
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Подождите...")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.note = note
        binding.editImageView.setOnClickListener {
            viewModel.toggleMode(binding.titleView.text.toString().isEmpty())
        }
        binding.removeImageView.setOnClickListener { viewModel.removeNote(note) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.actionLiveData.observe(viewLifecycleOwner, Observer {
            cancelProgress()
            when (it) {
                DetailViewModel.ACTION.PROGRESS -> showProgress()
                DetailViewModel.ACTION.SAVED -> showMessage("Данные успешно сохранены", context)
                DetailViewModel.ACTION.DELETE -> {
                    showMessage("Записать удалена", context)
                    activity?.onBackPressed()
                }
                DetailViewModel.ACTION.ERROR -> showMessage("Произошла ошибка", context)
                DetailViewModel.ACTION.EMPTY -> showMessage("Заголовок не может быть пустым", context)
            }
        })

        viewModel.noteLiveData.observe(viewLifecycleOwner, Observer { note = it })

        viewModel.editModeLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.editImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_save_white))
                binding.titleView.isEnabled = true
                binding.descTextView.isEnabled = true
                binding.titleView.requestFocus()
                note?.preview()
            } else {
                binding.editImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_mode_edit_white))
                binding.titleView.isEnabled = false
                binding.descTextView.isEnabled = false
                viewModel.saveNote(getNote())
            }
        })
        if (note?.id == null) {
            viewModel.toggleMode(false)
            binding.titleView.requestFocus()
        }
    }

    private fun getNote(): Note {
        if (note == null) {
            note = Note(
                binding.titleView.text.toString(),
                binding.descTextView.text.toString()
            )
        } else {
            note?.title = binding.titleView.text.toString()
            note?.desc = binding.descTextView.text.toString()
        }
        return note!!
    }


    private fun showProgress() {
        progressDialog.show()
    }

    private fun cancelProgress() {
        progressDialog.cancel()
        progressDialog.dismiss()
    }
}

fun newInstanceDetailFragment(note: Note?) = DetailFragment().apply {
    arguments = Bundle().apply {
        putParcelable(EXTRA_NOTE, note)
    }
}

fun showMessage(message: String, context: Context?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}