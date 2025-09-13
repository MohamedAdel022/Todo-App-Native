package com.route.todoapp.fragments.tasksList

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.route.todoapp.R
import com.route.todoapp.databinding.ItemTaskBinding
import com.route.todoapp.formatTime
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

    fun submitList(tasks: List<TaskDM>) {
        tasksList = tasks
        notifyDataSetChanged()
    }


    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(task: TaskDM)
        fun onDoneClick(task: TaskDM)
        fun onDeleteClick(task: TaskDM)
    }

    inner class TasksViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskDM) {
            binding.taskTitleTextView.text = task.title
            binding.taskTimeTextView.text = formatTime(task.dueDate)

            // Update UI based on completion status
            if (task.isCompleted) {
                binding.isDoneImageView.visibility = android.view.View.GONE
                binding.doneTextView.visibility = android.view.View.VISIBLE
                binding.taskTitleTextView.setTextColor( ContextCompat.getColor(
                    binding.root.context,
                    R.color.green
                ))
                binding.verticalLine.setBackgroundResource(R.drawable.vertical_line_rounded_green)
            } else {
                binding.isDoneImageView.visibility = android.view.View.VISIBLE
                binding.doneTextView.visibility = android.view.View.GONE
                binding.taskTitleTextView.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.sky_blue
                    )
                )
                binding.verticalLine.setBackgroundResource(R.drawable.vertical_line_rounded)
            }

            binding.draggedItem.setOnClickListener {
                onItemClickListener?.onItemClick(task)
            }
            binding.isDoneImageView.setOnClickListener {
                onItemClickListener?.onDoneClick(task)
            }
            binding.deleteBtn.setOnClickListener {
                onItemClickListener?.onDeleteClick(task)
            }
            binding.doneTextView.setOnClickListener {
                onItemClickListener?.onDoneClick(task)
            }
        }
    }
}
