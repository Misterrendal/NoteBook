package com.rendal.notebook.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rendal.notebook.data.dao.NoteDao
import com.rendal.notebook.data.entities.Note

val DATABASE_NAME = "note_database"
const val DATABASE_VERSION = 1

@Database(version = DATABASE_VERSION, entities = arrayOf(Note::class), exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
}