package com.canerture.firebaseexamples.presentation.done

import android.graphics.Paint
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

class DoneAdapter : ListAdapter<Todo, DoneAdapter.DoneViewHolder>(DiffCallback()) {

    var onEditClick: (String) -> Unit = {}
    var onNotDoneClick: (String) -> Unit = {}
    var onDeleteClick: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoneViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class DoneViewHolder(private var binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo) {

            with(binding) {

                tvTodo.paintFlags = tvTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
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
                        onNotDoneClick(documentId)
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