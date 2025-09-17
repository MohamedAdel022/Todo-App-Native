package com.route.todoapp.fragments.tasksList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import com.route.todoapp.R
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.databinding.CalendarDayLayoutBinding
import com.route.todoapp.databinding.FragmentTasksListBinding
import com.route.todoapp.databinding.ItemWeekHeaderBinding
import com.route.todoapp.models.TaskDM
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

class TasksListFragment : Fragment() {
    lateinit var binding: FragmentTasksListBinding
    var adapter = TasksListAdapter(emptyList())
    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCalenderView()
        initTasksListRecyclerView()
        refreshTasksList()
    }

    fun initCalenderView() {
        binding.WeekCalendarView.weekHeaderBinder =
            object : WeekHeaderFooterBinder<WeekDayHeaderViewHolder> {
                override fun create(view: View): WeekDayHeaderViewHolder {
                    val binding = ItemWeekHeaderBinding.bind(view)
                    return WeekDayHeaderViewHolder(binding)
                }

                override fun bind(
                    container: WeekDayHeaderViewHolder, data: Week
                ) {
                    container.binding.monthNameTextView.text =
                        data.days.first().date.month.getDisplayName(
                            TextStyle.FULL, Locale.getDefault()
                        )
                }
            }

        binding.WeekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewHolder> {
            override fun create(view: View): WeekDayViewHolder {
                val binding = CalendarDayLayoutBinding.bind(view)
                return WeekDayViewHolder(binding)
            }

            override fun bind(
                container: WeekDayViewHolder, data: WeekDay
            ) {
                val weekDayTextView = container.binding.dayNameTextView
                val monthDayTextView = container.binding.dayNumberTextView
                monthDayTextView.text = data.date.dayOfMonth.toString()
                weekDayTextView.text =
                    data.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

                // Set colors based on selection state
                val isSelected = selectedDate == data.date
                if (isSelected) {
                    monthDayTextView.setTextColor(
                        ResourcesCompat.getColor(resources, R.color.sky_blue, null)
                    )
                    weekDayTextView.setTextColor(
                        ResourcesCompat.getColor(resources, R.color.sky_blue, null)
                    )
                } else {
                    // Use theme-aware colors that work in both light and dark modes
                    val typedArray = requireContext().obtainStyledAttributes(intArrayOf(android.R.attr.textColorPrimary))
                    val textColor = typedArray.getColor(0, 0)
                    typedArray.recycle()

                    monthDayTextView.setTextColor(textColor)
                    weekDayTextView.setTextColor(textColor)
                }

                container.binding.root.setOnClickListener {
                    // Toggle selection: if clicked date is already selected, deselect it; otherwise select it
                    selectedDate = if (selectedDate == data.date) null else data.date
                    // Update the tasks list based on the selected date
                    val tasks = getTasksByDate(selectedDate)
                    adapter.submitList(tasks)

                    // Refresh the calendar to update visual state
                    binding.WeekCalendarView.notifyWeekChanged( data.date)
                }
            }
        }

        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(12).atStartOfMonth()
        val endDate = currentMonth.plusMonths(24).atEndOfMonth()
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        binding.WeekCalendarView.setup(startDate, endDate, firstDayOfWeek)
        binding.WeekCalendarView.scrollToWeek(currentDate)
    }

    fun refreshTasksList() {
        val tasks = getTasksByDate(selectedDate)

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


    fun getTasksByDate(date: LocalDate?): List<TaskDM> {
        val tasks = MyDatabase.getInstance().taskDao().getAllTasks()
        if (date != null) {
            return tasks.filter { task ->
                val taskDate = Instant.ofEpochMilli(task.dueDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                taskDate.isEqual(date)
            }
        }
        return tasks
    }

}
