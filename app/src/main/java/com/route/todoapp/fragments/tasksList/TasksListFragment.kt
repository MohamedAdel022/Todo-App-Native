package com.route.todoapp.fragments.tasksList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.databinding.FragmentTasksListBinding
import com.route.todoapp.models.TaskDM

class TasksListFragment : Fragment(){
    lateinit var binding: FragmentTasksListBinding
    var adapter = TasksListAdapter (emptyList())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTasksListRecyclerView()
        refreshTasksList()

    }

     fun refreshTasksList() {
       val tasks = MyDatabase.getInstance().taskDao().getAllTasks()
        adapter.submitList(tasks)
    }


    private fun initTasksListRecyclerView() {
        binding.TaskRecyclerView.adapter = adapter
        adapter.onItemClickListener = object : TasksListAdapter.OnItemClickListener {
            override fun onItemClick(task: TaskDM) {
                // Handle item click
            }

            override fun onDoneClick(task: TaskDM) {
                task.isCompleted = !task.isCompleted
                MyDatabase.getInstance().taskDao().updateTask(task)
                refreshTasksList()
            }

            override fun onDeleteClick(task: TaskDM) {
                MyDatabase.getInstance().taskDao().deleteTask(task)
                refreshTasksList()
            }
        }

    }
}