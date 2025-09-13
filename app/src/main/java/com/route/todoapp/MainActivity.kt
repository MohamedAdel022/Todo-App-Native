package com.route.todoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.databinding.ActivityMainBinding
import com.route.todoapp.fragments.addTask.AddTaskFragment
import com.route.todoapp.fragments.settings.SettingsFragment
import com.route.todoapp.fragments.tasksList.TasksListFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var tasksListFragment= TasksListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        showFragment(tasksListFragment)
    }

    fun showFragment( fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

   private fun initListeners() {
        binding.fabAddTask.setOnClickListener {
            AddTaskFragment{
                tasksListFragment.refreshTasksList()
            }.show(supportFragmentManager, "AddTaskFragment")
        }

       binding.bottomNavigationView.setOnItemSelectedListener {
           when(it.itemId) {
               R.id.navigation_tasks_list -> {
                   showFragment(TasksListFragment())

               }
               R.id.navigation_settings-> {
                    showFragment(SettingsFragment())
               }

           }

           return@setOnItemSelectedListener true

       }

    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}