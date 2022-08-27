package com.canerture.firebaseexamples.presentation.firestore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canerture.firebaseexamples.data.model.Contact
import com.canerture.firebaseexamples.databinding.ItemContactBinding

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private val list = ArrayList<Contact>()

    var onItemClick: (Contact) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ContactsViewHolder(private var binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Contact) {

            with(binding) {

                tvName.text = item.name
                tvSurname.text = item.surname
                tvEmail.text = item.email
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(updatedList: List<Contact>) {
        list.clear()
        list.addAll(updatedList)
        notifyItemRangeInserted(0, updatedList.size)
    }
}