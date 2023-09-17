package com.example.todoapp.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.todoapp.databinding.DialogAddTaskBinding
import com.example.todoapp.utils.validateEditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Date


class AddTaskBottomSheet(private val callBack: CallBack) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val frameLayout: FrameLayout? =
                dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            frameLayout?.setBackgroundResource(android.R.color.background_light)
        }
        binding = DialogAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

        // Save new task
        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
            val edtName = binding.taskNameEdt
            val edtNameL = binding.taskNameLayout
            val edtDescription = binding.taskDescriptionEdt
            val priority = binding.showPriority

            if (validateEditText(edtName, edtNameL)) {
                callBack.save(
                    edtName.text.toString(),
                    edtDescription.text.toString(),
                    Date(),
                    priority.text.toString()
                )
                dismiss()

            }
        }
    }
}

interface CallBack {
    fun save(name: String, description: String, time: Date, priority: String)
}