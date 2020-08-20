package br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.database.dao.TaskDao
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
}