package com.rendal.notebook.di.modules

import com.rendal.notebook.data.dao.NoteDao
import com.rendal.notebook.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DaoModule::class])
class NoteRepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository = NoteRepository(noteDao)
}