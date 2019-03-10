package com.rendal.notebook.di.modules

import com.rendal.notebook.data.NoteDatabase
import com.rendal.notebook.data.dao.NoteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
class DaoModule {

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao = noteDatabase.getNoteDao()
}