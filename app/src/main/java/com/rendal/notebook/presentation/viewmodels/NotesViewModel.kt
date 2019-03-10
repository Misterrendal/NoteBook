package com.rendal.notebook.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rendal.notebook.App
import com.rendal.notebook.data.entities.Note
import com.rendal.notebook.data.repository.NoteRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = (application as App).appComponent.getNoteRepository()
    private val compositeDisposable = CompositeDisposable()

    val messageLiveData = MutableLiveData<String>()

    val notesLiveData = MutableLiveData<List<Note>>()

    fun getNotes() {
        val notesDisposable = noteRepository.getNotes()
            .subscribeOn(Schedulers.io())
            .subscribe({
                notesLiveData.postValue(it)
            }, { Timber.e(it) })

        compositeDisposable.add(notesDisposable)
    }

    fun removeNote(note: Note) {
        val removeDisposable = noteRepository.removeNote(note)
            .subscribeOn(Schedulers.io())
            .subscribe({ messageLiveData.postValue("Заметка успешно удалена") }, { Timber.d(it) })

        compositeDisposable.add(removeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}