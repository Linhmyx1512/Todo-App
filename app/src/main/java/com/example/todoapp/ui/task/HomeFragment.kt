package com.example.todoapp.ui.task

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.todoapp.adapters.TaskPagerAdapter
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.ui.add.AddTaskBottomSheet
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.viewmodels.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var taskBinding: FragmentHomeBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private val fragmentList = listOf(
        AllTaskFragment(),
        OnProgressFragment(),
        CompletedFragment()
    )

    private val taskViewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        taskBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return taskBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskBinding = FragmentHomeBinding.bind(view)
        initTaskPagerAdapter()
        setOnClickListener()
    }

    private fun initTaskPagerAdapter() {
        tabLayout = taskBinding.tabLayout
        viewPager = taskBinding.viewPager.apply {
            adapter = TaskPagerAdapter(this@HomeFragment, fragmentList)
            isUserInputEnabled = false
        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                1 -> tab.text = "On Progress"
                2 -> tab.text = "Completed Tasks"
                else -> tab.text = "All Tasks"
            }
        }.attach()
    }


    private fun callSortByDialog() {
        var checkedItem = 0
        val items =
            arrayOf("Title Ascending", "Title Descending", "Date Ascending", "Date Descending")
        taskBinding.iconSort.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sort By")
                .setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ok") { dialog, _ ->
                    when (checkedItem) {
                        0 -> taskViewModel.setSortBy(Pair("title", true))
                        1 -> taskViewModel.setSortBy(Pair("title", false))
                        2 -> taskViewModel.setSortBy(Pair("date", true))
                        3 -> taskViewModel.setSortBy(Pair("date", false))
                    }
                    dialog.dismiss()
                }
                .setSingleChoiceItems(items, checkedItem) { _, which ->
                    checkedItem = which
                }
                .show()
        }
    }


    private fun setOnClickListener() {

        // Press btn add to show dialog add new task
        taskBinding.btnAdd.setOnClickListener {
            AddTaskBottomSheet(object : CallBack {
                override fun save(task: Task) {
                    requireContext().hideKeyBoard(it)
                    taskViewModel.insertTask(task)
                }
            }).show(childFragmentManager, "Show dialog add task")
        }

        // Search task
        taskBinding.searchBar.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(query: Editable) {
                    if (query.toString().isNotEmpty()) {
                        taskViewModel.searchTask(query.toString())
                    } else {
                        taskViewModel.setSortBy(Pair("title", true))
                    }
                }
            })

            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requireContext().hideKeyBoard(v)
                    return@setOnEditorActionListener true
                }
                false
            }
        }
        callSortByDialog()
    }
}