package com.route.todoapp.fragments.addTask

import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.Locale
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.route.todoapp.R
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.databinding.FragmentAddTaskBinding
import com.route.todoapp.models.TaskDM

class AddTaskFragment(var onDismiss: () -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private var selectData: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        dialog?.setOnShowListener { dialogInterface ->
            val dialog = dialogInterface as Dialog
            dialog.setCanceledOnTouchOutside(true)
            dialog.setCancelable(true)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        bindDate()
        bindTime()
    }

    private fun bindTime() {
        binding.selectTimeTextView.text = selectData.getFormattedTime()
    }

    private fun bindDate() {
        binding.selectDateTextView.text = selectData.getFormattedDate()
    }

    private fun initListeners() {
        binding.addTaskButton.setOnClickListener {
            if (!isValidForm()) {
                return@setOnClickListener
            }

            val newTask = TaskDM(
                title = binding.addTaskTitleEditText.text.toString(),
                description = binding.addTaskDescriptionEditText.text.toString(),
                dueDate = selectData.timeInMillis,
                isCompleted = false
            )
            MyDatabase.getInstance().taskDao().insertTask(newTask)
            onDismiss()
            dismiss()
        }

        binding.selectDateTextView.setOnClickListener {
            val datePickerDialog = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(selectData.timeInMillis)
                .build()

            datePickerDialog.addOnPositiveButtonClickListener { selection ->
                // Preserve the existing time when setting new date
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.timeInMillis = selection

                // Preserve the current time
                val currentHour = selectData.get(Calendar.HOUR_OF_DAY)
                val currentMinute = selectData.get(Calendar.MINUTE)
                selectedCalendar.set(Calendar.HOUR_OF_DAY, currentHour)
                selectedCalendar.set(Calendar.MINUTE, currentMinute)

                selectData = selectedCalendar
                bindDate()
            }
            datePickerDialog.show(parentFragmentManager, "datePicker")
        }

        binding.selectTimeTextView.setOnClickListener {
            val timePickerDialog = MaterialTimePicker.Builder()
                .setTitleText("Select Time")
                .setHour(selectData.get(Calendar.HOUR_OF_DAY))
                .setMinute(selectData.get(Calendar.MINUTE))
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .build()

            timePickerDialog.addOnPositiveButtonClickListener {
                selectData.set(Calendar.HOUR_OF_DAY, timePickerDialog.hour)
                selectData.set(Calendar.MINUTE, timePickerDialog.minute)
                bindTime()
            }
            timePickerDialog.show(parentFragmentManager, "timePicker")
        }
    }

    private fun isValidForm(): Boolean {
        var isValid = true
        if (binding.addTaskTitleEditText.text.isNullOrEmpty()) {
            binding.addTaskTitleEditText.error = getString(R.string.required_field)
            isValid = false
        }
        if (binding.addTaskDescriptionEditText.text.isNullOrEmpty()) {
            binding.addTaskDescriptionEditText.error = getString(R.string.required_field)
            isValid = false
        }
        return isValid
    }
}

fun Calendar.getFormattedDate(): String {
    val year = this.get(Calendar.YEAR)
    val month = this.get(Calendar.MONTH) + 1 // Months are 0-based in Calendar
    val day = this.get(Calendar.DAY_OF_MONTH)
    return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year)
}

fun Calendar.getFormattedTime(): String {
    val hour = this.get(Calendar.HOUR)
    val minute = this.get(Calendar.MINUTE)
    val amPm = if (this.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
    val displayHour = if (hour == 0) 12 else hour
    return String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)
}
