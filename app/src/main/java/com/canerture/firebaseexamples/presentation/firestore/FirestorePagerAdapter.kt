package com.canerture.firebaseexamples.presentation.firestore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.canerture.firebaseexamples.presentation.firestore.alltodos.AllTodosFragment
import com.canerture.firebaseexamples.presentation.firestore.done.DoneTodosFragment
import com.canerture.firebaseexamples.presentation.firestore.importanttodos.ImportantTodosFragment
import com.canerture.firebaseexamples.presentation.firestore.todos.TodosFragment

class FirestorePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return TodosFragment()
            1 -> return ImportantTodosFragment()
            2 -> return DoneTodosFragment()
            3 -> return AllTodosFragment()
        }
        return TodosFragment()
    }
}