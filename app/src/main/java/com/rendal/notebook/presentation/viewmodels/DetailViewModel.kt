package com.rendal.notebook.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rendal.notebook.App
import com.rendal.notebook.data.entities.Note
import com.rendal.notebook.data.repository.NoteRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = (application as App).appComponent.getNoteRepository()
    private val compositeDisposable = CompositeDisposable()

    val noteLiveData = MutableLiveData<Note>()
    val actionLiveData = MutableLiveData<ACTION>()
    val editModeLiveData = MutableLiveData<Boolean>()

    fun saveNote(note: Note) {
        val insertDisposable = noteRepository.saveNote(note)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { actionLiveData.postValue(ACTION.PROGRESS) }
            .doOnSuccess { actionLiveData.postValue(ACTION.SAVED) }
            .subscribe({ noteLiveData.postValue(it) }, { actionLiveData.postValue(ACTION.ERROR) })

        compositeDisposable.add(insertDisposable)
    }

    fun toggleMode(emptyTitle: Boolean) {
        if (editModeLiveData.value == true && emptyTitle)
            actionLiveData.value = ACTION.EMPTY
        if (editModeLiveData.value == null)
            editModeLiveData.value = true
        else
            editModeLiveData.value = !(editModeLiveData.value as Boolean)
    }

    fun removeNote(note: Note?) {
        if (note?.id != null) {
            val removeDisposable = noteRepository.removeNote(note)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { actionLiveData.postValue(ACTION.PROGRESS) }
                .doOnComplete { actionLiveData.postValue(ACTION.DELETE) }
                .subscribe({}, { actionLiveData.postValue(ACTION.ERROR) })

            compositeDisposable.add(removeDisposable)
        }

    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    enum class ACTION {
        PROGRESS, SAVED, ERROR, DELETE, EMPTY
    }
}