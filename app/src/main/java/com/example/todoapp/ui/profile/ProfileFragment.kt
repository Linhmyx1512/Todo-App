package com.example.todoapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todoapp.databinding.FragmentProfileBinding
import com.example.todoapp.viewmodels.TaskViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        taskViewModel.doneTasks.observe(viewLifecycleOwner) { doneTasks ->
            binding.sizeDone.text = doneTasks.size.toString()
        }
        taskViewModel.notDoneTasks.observe(viewLifecycleOwner) { notDoneTasks ->
            binding.sizeNotDone.text = notDoneTasks.size.toString()
        }
    }
}