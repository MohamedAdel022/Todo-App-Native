package com.route.todoapp.fragments.tasksList

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.route.todoapp.databinding.ItemTaskBinding
import com.route.todoapp.models.TaskDM

class TasksListAdapter(var tasksList: List<TaskDM>) :
    RecyclerView.Adapter<TasksListAdapter.TasksViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(
            android.view.LayoutInflater.from(parent.context), parent, false
        )
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TasksViewHolder, position: Int
    ) {
        holder.bind(tasksList[position])
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(task: TaskDM)
        fun onDoneClick(task: TaskDM)
    }

    inner class TasksViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskDM) {
            binding.taskTitleTextView.text = task.title
            binding.taskTimeTextView.text = task.dueDate.toString()
            binding.root.setOnClickListener {
                 onItemClickListener?.onItemClick(task)

            }
            binding.isDoneImageView.setOnClickListener {
                onItemClickListener?.onDoneClick(task)

            }
        }

    }
    }