package com.example.todoapp.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TaskPagerAdapter(fragment: Fragment, private val fragmentList: List<Fragment>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}