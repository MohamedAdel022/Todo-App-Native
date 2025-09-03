package com.route.todoapp.fragments.addTask

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.route.todoapp.databinding.FragmentAddTaskBinding

class AddTaskFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddTaskBinding
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
       Log.e("TAG", "onViewCreated: ", )
    }
}