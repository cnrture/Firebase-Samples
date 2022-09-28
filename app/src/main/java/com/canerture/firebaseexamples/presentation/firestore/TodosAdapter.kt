package com.canerture.firebaseexamples.presentation.firestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canerture.firebaseexamples.common.Constants.PRIORITY_HIGH
import com.canerture.firebaseexamples.common.Constants.PRIORITY_LOW
import com.canerture.firebaseexamples.common.Constants.PRIORITY_MEDIUM
import com.canerture.firebaseexamples.data.model.Todo
import com.canerture.firebaseexamples.databinding.ItemTodoBinding

class TodosAdapter : RecyclerView.Adapter<TodosAdapter.TodoViewHolder>() {

    private val list = ArrayList<Todo>()

    var onEditClick: (String) -> Unit = {}

    var onDoneClick: (Boolean, String) -> Unit = { _, _ -> }

    var onDeleteClick: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class TodoViewHolder(private var binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Todo) {

            with(binding) {

                tvTodo.text = item.todo
                tvDate.text = item.date

                item.documentId?.let { documentId ->

                    when(item.priority) {
                        PRIORITY_LOW -> tvLowPriority.visibility = View.VISIBLE
                        PRIORITY_MEDIUM -> tvMediumPriority.visibility = View.VISIBLE
                        PRIORITY_HIGH -> tvHighPriority.visibility = View.VISIBLE
                    }

                    cbDone.isChecked = item.isDone

                    cbDone.setOnCheckedChangeListener { _, isChecked ->
                        onDoneClick(isChecked, documentId)
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

    override fun getItemCount(): Int = list.size

    fun updateList(updatedList: List<Todo>) {
        list.clear()
        list.addAll(updatedList)
        notifyItemRangeInserted(0, updatedList.size)
    }
}