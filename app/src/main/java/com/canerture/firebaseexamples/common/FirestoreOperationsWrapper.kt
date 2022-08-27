package com.canerture.firebaseexamples.common

import com.canerture.firebaseexamples.data.model.Contact
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreOperationsWrapper @Inject constructor(firestore: FirebaseFirestore) {

    private val collection = firestore.collection("Contacts")

    fun addData(contact: Contact, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        collection.add(contact)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun setData(
        contact: Contact,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.document(contact.name ?: "Person").set(contact)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun updateData(
        contact: Contact,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        val contactRef = collection.document(contact.name.orEmpty())

        val hashMap = mapOf(
            "surname" to contact.surname,
            "email" to contact.email
        )

        contactRef.update(hashMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun getDataWithRealtimeUpdates(
        onSuccess: (List<Contact>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.addSnapshotListener { snapshot, error ->

            snapshot?.toObjects(Contact::class.java)?.let { onSuccess(it) }

            error?.let { onFailure(it.message.orEmpty()) }
        }
    }
}