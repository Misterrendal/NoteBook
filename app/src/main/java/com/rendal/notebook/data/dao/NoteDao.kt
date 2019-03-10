package com.rendal.notebook.data.dao

import androidx.room.*
import com.rendal.notebook.data.entities.Note
import io.reactivex.Flowable
import io.reactivex.Single


const val TABLE_NAME = "note"
const val id = "id"

@Dao
abstract class NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNote(note: Note): Long

    @Transaction
    open fun saveNote(note: Note): Note =
        if (note.id == null) {
            val idNote = insertNote(note)
            getNoteByID(idNote)
        } else {
            updateNote(note)
            note
        }


    @Delete
    abstract fun deleteNote(note: Note)

    @Update
    abstract fun updateNote(note: Note): Int

    @Query("SELECT * from $TABLE_NAME")
    abstract fun getNotes(): Flowable<List<Note>>

    @Query("SELECT * from $TABLE_NAME WHERE $id=:ID")
    abstract fun getNoteByID(ID: Long): Note
}