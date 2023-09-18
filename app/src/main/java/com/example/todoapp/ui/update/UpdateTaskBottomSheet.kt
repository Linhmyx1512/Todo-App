package com.example.todoapp.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.DialogUpdateTaskBinding
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.utils.validateEditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Date


class UpdateTaskBottomSheet(private val callBack: CallBack) : BottomSheetDialogFragment() {

    private lateinit var task: Task
    private lateinit var binding: DialogUpdateTaskBinding


    companion object {
        fun newInstance(task: Task, callBack: CallBack) = UpdateTaskBottomSheet(callBack).apply {
            this.task = task
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val frameLayout: FrameLayout? =
                dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            frameLayout?.setBackgroundResource(android.R.color.background_light)
        }
        binding = DialogUpdateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPropertyToDialog()
        setOnClickListener()
    }

    private fun setPropertyToDialog() {
        binding.taskNameEdt.setText(task.title)
        binding.taskDescriptionEdt.setText(task.description)
        binding.showTime.text = task.dueTime.toString()
        binding.showPriority.text = task.priority
    }

    private fun setOnClickListener() {

        // Save edit task
        val btnSave = binding.btnSave
        btnSave.setOnClickListener {
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
