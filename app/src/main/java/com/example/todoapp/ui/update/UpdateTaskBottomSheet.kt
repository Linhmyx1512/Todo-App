package com.example.todoapp.ui.update

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.DialogUpdateTaskBinding
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.utils.validateEditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class UpdateTaskBottomSheet(private val callBack: CallBack) : BottomSheetDialogFragment() {

    private lateinit var task: Task
    private lateinit var binding: DialogUpdateTaskBinding
    private var date = Date()


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
        setUp()
        setOnClickListener()
    }

    private fun setUp() {
        val myAdapter = ArrayAdapter(
            requireContext(),
            com.example.todoapp.R.layout.list_priority,
            resources.getStringArray(com.example.todoapp.R.array.priorities)
        )
        binding.showPriority.setAdapter(myAdapter)
        setPropertyToDialog()
    }

    private fun setPropertyToDialog() {
        binding.taskNameEdt.setText(task.title)
        binding.taskDescriptionEdt.setText(task.description)
        binding.showTime.text = SimpleDateFormat(
            "dd-MMM-yyyy HH:mm",
            Locale.getDefault()
        ).format(task.dueTime)
        binding.showPriority.setText(task.priority, false)
    }

    private fun showDateTimePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        date = calendar.time
                        binding.showTime.text = SimpleDateFormat(
                            "dd-MMM-yyyy HH:mm",
                            Locale.getDefault()
                        ).format(date)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setOnClickListener() {
        // Show time picker
        binding.showTime.apply {
            setOnClickListener {
                showDateTimePicker()
            }
        }

        // Save edit task
        val btnSave = binding.btnSave
        btnSave.setOnClickListener {
            val edtName = binding.taskNameEdt
            val edtNameL = binding.taskNameLayout
            val edtDescription = binding.taskDescriptionEdt
            var priority = binding.showPriority.text.toString()
            if (priority == "Priority")
                priority = "High"

            if (validateEditText(edtName, edtNameL)) {
                requireContext().hideKeyBoard(it)
                callBack.save(
                    Task(
                        task.id,
                        edtName.text.toString(),
                        edtDescription.text.toString(),
                        Date(),
                        priority
                    )
                )
                dismiss()
            }
        }
    }
}
