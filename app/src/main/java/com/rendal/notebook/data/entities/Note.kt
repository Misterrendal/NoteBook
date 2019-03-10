package com.rendal.notebook.data.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Note(
    var title: String,
    var desc: String
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var date: Long
    @Ignore
    var memento: Memento? = null

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        date = parcel.readLong()
    }

    init {
        date = System.currentTimeMillis()
    }


    fun preview() {
        memento = Memento(title, desc)
    }

    fun undo() {
        if (memento != null) {
            title = memento!!.title
            desc = memento!!.desc
        }
    }

    fun getDateFormat(): String {
        val dateFormat = SimpleDateFormat("dd MMMM HH:mm", Locale.getDefault())
        return dateFormat.format(Date(date))
    }

    override fun equals(other: Any?): Boolean = id == (other as Note).id


    override fun toString(): String = "$id $title $desc $date"
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(desc)
        parcel.writeValue(id)
        parcel.writeLong(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }


}

class Memento(
    var title: String,
    val desc: String
)