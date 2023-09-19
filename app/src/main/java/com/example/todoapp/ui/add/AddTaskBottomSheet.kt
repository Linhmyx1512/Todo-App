package com.example.todoapp.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.example.todoapp.R
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
        val myAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.priorities)
        )
        myAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
        binding.showPriority.adapter = myAdapter

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // Save new task
        val btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
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

interface CallBack {
    fun save(name: String, description: String, time: Date, priority: String)
}