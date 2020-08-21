package br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Task (
    var title: String,
    var description : String,
    var status: Boolean
){
    var id: Long = 0
}