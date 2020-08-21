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
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.api.TaskService
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model.Task
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() , TaskAdapterListener{
    private lateinit var adapter: TaskAdapter
    private lateinit var service: TaskService
    private lateinit var retrofit: Retrofit
    private lateinit var tasks: MutableList<Task>
    private var removeFromList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofitStart()

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
        loadData()
    }

    override fun taskSave(task: Task) {
        service.insert(task).enqueue(object : Callback<Task>{
            override fun onFailure(call: Call<Task>, t: Throwable) {}
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                val insertedTask = response.body()!!
                task.id = insertedTask.id
                tasks.add(0, task)
            }
        })
        enableRemoveFromList(false)
        loadData()
    }

    override fun taskChange(task: Task) {
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
//        val tasks = dao.getAll().toMutableList()

        adapter = TaskAdapter(tasks.toMutableList(), this, applicationContext)
        rc_tasks.adapter = adapter
        rc_tasks.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }
    private fun retrofitStart(){
        retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(TaskService::class.java)
        tasks = mutableListOf()

        service.getAll().enqueue(object : Callback<List<Task>> {
            override fun onFailure(call: Call<List<Task>>, t: Throwable) {}
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                tasks = response.body()!!.toMutableList()
                loadData()
            }
        })
    }
}