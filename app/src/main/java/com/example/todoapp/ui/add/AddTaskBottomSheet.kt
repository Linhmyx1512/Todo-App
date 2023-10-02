package com.example.todoapp.ui.add

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import com.example.todoapp.R
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.DialogAddTaskBinding
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.utils.validateEditText
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddTaskBottomSheet(private val callBack: CallBack) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogAddTaskBinding
    private lateinit var behavior: BottomSheetBehavior<*>
    private var date = Date()
    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        behavior = dialog.behavior
        behavior.skipCollapsed = true
        return dialog
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setOnShowListener {
            val frameLayout: FrameLayout? =
                dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            frameLayout?.setBackgroundResource(android.R.color.transparent)
        }
        binding = DialogAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpinner()
        setOnClickListener()
    }

    private fun setUpSpinner() {
        val myAdapter = ArrayAdapter(
            requireContext(),
            R.layout.list_priority,
            resources.getStringArray(R.array.priorities)
        )
        binding.showPriority.setAdapter(myAdapter)
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
        // Init date
        binding.showTime.text = SimpleDateFormat(
            "dd-MMM-yyyy HH:mm",
            Locale.getDefault()
        ).format(date)

        // Show time picker
        binding.showTime.apply {
            setOnClickListener {
                showDateTimePicker()
            }

        }

        // Save new task
        binding.btnAdd.setOnClickListener {
            val edtName = binding.taskNameEdt
            val edtNameL = binding.taskNameLayout
            val edtDescription = binding.taskDescriptionEdt
            val priority = if (binding.showPriority.isSelected) {
                binding.showPriority.text.toString()
            } else {
                "High"
            }
            if (validateEditText(edtName, edtNameL)) {
                requireContext().hideKeyBoard(it)
                callBack.save(
                    Task(
                        0,
                        edtName.text.toString(),
                        edtDescription.text.toString(),
                        date,
                        priority
                    )
                )
                dismiss()
            }
        }
    }
}

interface CallBack {
    fun save(task: Task)
}