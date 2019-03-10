package com.rendal.notebook.di.modules

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rendal.notebook.data.DATABASE_NAME
import com.rendal.notebook.data.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ContextModule::class])
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): NoteDatabase =
        Room.databaseBuilder(context, NoteDatabase::class.java, DATABASE_NAME).build()

}