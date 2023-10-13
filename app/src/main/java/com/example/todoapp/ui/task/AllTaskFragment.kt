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
import com.example.todoapp.databinding.FragmentAllTaskBinding
import com.example.todoapp.ui.add.AddTaskBottomSheet
import com.example.todoapp.ui.add.CallBack
import com.example.todoapp.utils.hideKeyBoard
import com.example.todoapp.viewmodels.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class AllTaskFragment : Fragment() {

    private lateinit var taskBinding: FragmentAllTaskBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private val fragmentList = listOf(
        OnProgressFragment(),
        CompletedFragment()
    )

    private val taskViewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        taskBinding = FragmentAllTaskBinding.inflate(layoutInflater, container, false)

        return taskBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskBinding = FragmentAllTaskBinding.bind(view)
        initTaskPagerAdapter()
        setOnClickListener()
    }

    private fun initTaskPagerAdapter() {
        tabLayout = taskBinding.tabLayout
        viewPager = taskBinding.viewPager.apply {
            adapter = TaskPagerAdapter(this@AllTaskFragment, fragmentList)
            isUserInputEnabled = false
        }
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "On Progress"
                1 -> tab.text = "Completed Tasks"
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
                .setPositiveButton("Ok") { _, _ ->
                    when (checkedItem) {
                        0 -> {
                            taskViewModel.setSortBy(Pair("title", true))
                        }

                        1 -> {
                            taskViewModel.setSortBy(Pair("title", false))
                        }

                        2 -> {
                            taskViewModel.setSortBy(Pair("date", true))
                        }

                        else -> {
                            taskViewModel.setSortBy(Pair("date", false))
                        }
                    }
                }
                .setSingleChoiceItems(items, checkedItem) { _, selectedItemIndex ->
                    checkedItem = selectedItemIndex
                }
                .setCancelable(false)
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