package com.route.todoapp.fragments.tasksList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.route.todoapp.databinding.FragmentTasksListBinding
import com.route.todoapp.models.TaskDM

class TasksListFragment : Fragment(){
    lateinit var binding: FragmentTasksListBinding
    var tasksListAdapter= TasksListAdapter (emptyList())

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

    }

    private fun initTasksListRecyclerView() {
        binding.TaskRecyclerView.adapter = tasksListAdapter
        tasksListAdapter.tasksList = listOf(
            TaskDM(
                id = 1,
                title = "Complete project presentation",
                description = "Prepare slides and practice presentation for the upcoming project review meeting",
                dueDate = System.currentTimeMillis() + (24 * 60 * 60 * 1000), // Tomorrow
                isCompleted = false
            ),
            TaskDM(
                id = 2,
                title = "Buy groceries",
                description = "Get milk, bread, eggs, and vegetables from the supermarket",
                dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // Next week
                isCompleted = true
            ),
            TaskDM(
                id = 3,
                title = "Schedule dentist appointment",
                description = "Call the dental clinic to schedule a regular checkup appointment",
                dueDate = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000), // In 3 days
                isCompleted = false
            )
        )

        tasksListAdapter.notifyDataSetChanged()
    }
}