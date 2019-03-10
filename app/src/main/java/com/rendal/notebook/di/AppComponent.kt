package com.rendal.notebook.di.modules

import com.rendal.notebook.data.repository.NoteRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NoteRepositoryModule::class))
interface AppComponent {
    fun getNoteRepository(): NoteRepository
}