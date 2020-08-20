package br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.adapters.TaskAdapter
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.adapters.TaskAdapterListener
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.database.AppDatabase
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.database.dao.TaskDao
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , TaskAdapterListener{
    private lateinit var adapter: TaskAdapter
    private lateinit var dao: TaskDao
    private var removeFromList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tasks-db")
            .allowMainThreadQueries()
            .build()
        dao = db.taskDao()

        fab_AddTask.setOnClickListener{
            if (!removeFromList) {
                enableRemoveFromList(true)
                val task = Task("", "", false)
                task.id = 0L
                adapter.addTask(task)
            }
        }
        loadData()
    }

    override fun taskRemoved(task: Task) {
        enableRemoveFromList(false)
        dao.delete(task)
        loadData()
    }

    override fun taskSave(task: Task) {
        enableRemoveFromList(false)
        dao.insert(task)
        loadData()
    }

    override fun taskChange(task: Task) {
        dao.update(task)
        loadData()
    }

    override fun share(task: Task) {
        val subject: String = getString(R.string.subject)
        val  textExtra = "${getString(R.string.text_extra)} ${task.title}"
        val share = Intent(Intent.ACTION_SEND)
        with(share){
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT,  textExtra)
        }
        startActivity(share)
    }

    override fun enableRemoveFromList(flag: Boolean) {
        removeFromList = flag
    }

    override fun notificationAlert() {
        val textAlert: String = getString(R.string.notificationError)
        Toast.makeText(applicationContext,textAlert, Toast.LENGTH_SHORT).show()
    }

    private fun loadData(){
        val tasks = dao.getAll().toMutableList()

        adapter = TaskAdapter(tasks.toMutableList(), this, applicationContext)
        rc_tasks.adapter = adapter
        rc_tasks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

}