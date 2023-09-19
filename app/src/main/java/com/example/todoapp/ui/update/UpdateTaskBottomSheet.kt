package com.example.todoapp.ui.update

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
        val myAdapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            resources.getStringArray(com.example.todoapp.R.array.priorities)
        )
        myAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        binding.showPriority.adapter = myAdapter
        setPropertyToDialog()
        setOnClickListener()
    }

    private fun setPropertyToDialog() {
        binding.taskNameEdt.setText(task.title)
        binding.taskDescriptionEdt.setText(task.description)
        binding.showTime.text = task.dueTime.toString()
    }

    private fun setOnClickListener() {

        // Save edit task
        val btnSave = binding.btnSave
        btnSave.setOnClickListener {
            val edtName = binding.taskNameEdt
            val edtNameL = binding.taskNameLayout
            val edtDescription = binding.taskDescriptionEdt
            val priority = when (binding.showPriority.selectedItemPosition) {
                0 -> "High"
                1 -> "Medium"
                2 -> "Low"
                else -> "High"
            }

            if (validateEditText(edtName, edtNameL)) {
                callBack.save(
                    edtName.text.toString(),
                    edtDescription.text.toString(),
                    Date(),
                    priority
                )
                dismiss()
            }
        }
    }
}
