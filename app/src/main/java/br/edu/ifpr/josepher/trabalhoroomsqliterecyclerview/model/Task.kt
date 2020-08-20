package br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description : String,
    @ColumnInfo(name = "status")
    var status: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}