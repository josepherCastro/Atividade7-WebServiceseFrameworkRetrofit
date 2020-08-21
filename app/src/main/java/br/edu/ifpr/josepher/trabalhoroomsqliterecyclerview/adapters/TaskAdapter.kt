package br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.adapters

import android.content.Context
import android.graphics.Color.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.R
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.api.TaskService
import br.edu.ifpr.josepher.trabalhoroomsqliterecyclerview.model.Task
import kotlinx.android.synthetic.main.edit_one_task.view.*
import kotlinx.android.synthetic.main.one_task.view.*
import retrofit2.Retrofit

class TaskAdapter(val tasks: MutableList<Task>, val listener: TaskAdapterListener, context: Context):
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    var taskEditing : Task? = null
    var lastNewTaskEditing : Task? = null

    fun addTask(task: Task){
        taskEditing=task
        lastNewTaskEditing = task
        tasks.add(0, task)
        notifyItemInserted(0)
    }

    override fun getItemViewType(position: Int): Int {
        val task = tasks[position]
        return if(task == taskEditing)
            R.layout.edit_one_task
        else
            R.layout.one_task
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false))

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task =  tasks[position]
        holder.fillView(task)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun fillView(task: Task){
            var mycard = itemView as CardView

            if (task == taskEditing){
                itemView.etTitle.setText(task.title)
                itemView.etDescription.setText(task.description)

                itemView.btSave.setOnClickListener{
                    if(taskEditing != null && taskEditing!!.id == 0L){
                        task.title = itemView.etTitle.text.toString()
                        task.description = itemView.etDescription.text.toString()
                        task.status = false

                        if(task.title != ""){
                            with(this@TaskAdapter){
                                listener.taskSave(task)
                            }
                            notifyItemInserted(tasks.indexOf(task))
                        }else{
                            listener.notificationAlert()
                        }
                    }else{
                        task.title = itemView.etTitle.text.toString()
                        task.description = itemView.etDescription.text.toString()
                        if(task.title != "") {
                            with(this@TaskAdapter) {
                                listener.taskChange(task)
                            }
                            notifyItemChanged(tasks.indexOf(task))
                        }else{
                            listener.notificationAlert()
                        }
                    }
                }
                itemView.btDelete.setOnClickListener {
                    if(taskEditing != null && taskEditing!!.id == 0L) {
                        taskEditing = null
                        listener.enableRemoveFromList(false)
                        tasks.remove(task)
                        notifyItemRemoved(tasks.indexOf(task))
                    }else {
                        with(this@TaskAdapter) {
                            listener.taskRemoved(task)
                        }
                        notifyItemRemoved(tasks.indexOf(task))
                    }
                }
            }else{
                itemView.tvTitle.text = task.title

                if(task.status){
                    mycard.setCardBackgroundColor(parseColor("#A3BFA8"))
                }else{
                    mycard.setCardBackgroundColor(parseColor("#BE6E46"))
                    itemView.btShare.visibility = View.INVISIBLE
                }
                itemView.btShare.setOnClickListener {
                    listener.share(task)
                }
                itemView.setOnClickListener{
                    if(taskEditing!!.id == 0L) {
                        taskEditing = null
                        listener.enableRemoveFromList(false)
                        tasks.remove(lastNewTaskEditing)
                        notifyItemRemoved(tasks.indexOf(lastNewTaskEditing))
                    }
                    taskEditing = task
                    notifyItemChanged(tasks.indexOf(task))
                }
                itemView.setOnLongClickListener {
                    task.status = !task.status

                    listener.taskChange(task)
                    notifyItemChanged(tasks.indexOf(task))
                    true
                }
            }
        }
    }
}