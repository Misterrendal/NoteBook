package com.rendal.notebook.data.repository

import com.rendal.notebook.data.dao.NoteDao
import com.rendal.notebook.data.entities.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class NoteRepository(private val noteDao: NoteDao) {

    fun saveNote(note: Note): Single<Note> = Single.fromCallable {
        noteDao.saveNote(note)
    }

    fun removeNote(note: Note): Completable = Completable.fromCallable { noteDao.deleteNote(note) }

    fun getNotes(): Flowable<List<Note>> = noteDao.getNotes()
}