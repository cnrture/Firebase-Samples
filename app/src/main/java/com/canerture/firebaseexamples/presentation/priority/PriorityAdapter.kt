package com.canerture.firebaseexamples.presentation.priority

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canerture.firebaseexamples.common.Constants.PRIORITY_HIGH
import com.canerture.firebaseexamples.common.Constants.PRIORITY_LOW
import com.canerture.firebaseexamples.common.Constants.PRIORITY_MEDIUM
import com.canerture.firebaseexamples.common.visible
import com.canerture.firebaseexamples.data.model.Todo
import com.canerture.firebaseexamples.databinding.ItemTodoBinding

class PriorityAdapter(
    val onEditClick: (String) -> Unit,
    val onDoneClick: (String) -> Unit,
    val onDeleteClick: (String) -> Unit
) : ListAdapter<Todo, PriorityAdapter.TodoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class TodoViewHolder(private var binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo) {

            with(binding) {

                tvTodo.text = item.todo
                tvDate.text = item.date

                item.documentId?.let { documentId ->

                    when (item.priority) {
                        PRIORITY_LOW -> tvLowPriority.visible()
                        PRIORITY_MEDIUM -> tvMediumPriority.visible()
                        PRIORITY_HIGH -> tvHighPriority.visible()
                    }

                    cbDone.isChecked = item.isDone

                    cbDone.setOnCheckedChangeListener { _, _ ->
                        onDoneClick(documentId)
                    }

                    imgEdit.setOnClickListener {
                        onEditClick(documentId)
                    }

                    imgDelete.setOnClickListener {
                        onDeleteClick(documentId)
                    }
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem.documentId == newItem.documentId

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}