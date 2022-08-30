package com.canerture.firebaseexamples.common

import com.canerture.firebaseexamples.data.model.Contact
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreOperationsWrapper @Inject constructor(firestore: FirebaseFirestore) {

    private val collection = firestore.collection("Contacts")

    fun addData(
        contact: Contact,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val contactModel = hashMapOf(
            "name" to contact.name,
            "surname" to contact.surname,
            "email" to contact.email
        )

        collection.add(contactModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun setData(
        contact: Contact,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val contactModel = hashMapOf(
            "name" to contact.name,
            "surname" to contact.surname,
            "email" to contact.email
        )

        collection.document(contact.name ?: "Person").set(contactModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun updateData(
        contact: Contact,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val hashMap = mapOf(
            "name" to contact.name,
            "surname" to contact.surname,
            "email" to contact.email
        )

        collection.document(contact.documentId.orEmpty()).update(hashMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun getDataOnce(
        onSuccess: (List<Contact>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.get().addOnSuccessListener {

            val tempList = arrayListOf<Contact>()

            it.forEach { document ->
                tempList.add(
                    Contact(
                        document.id,
                        document.get("name") as String,
                        document.get("surname") as String,
                        document.get("email") as String
                    )
                )
            }

            onSuccess(tempList)
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun getDataWithRealtimeUpdates(
        onSuccess: (List<Contact>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.addSnapshotListener { snapshot, error ->

            val tempList = arrayListOf<Contact>()

            snapshot?.let {
                it.forEach { document ->
                    tempList.add(
                        Contact(
                            document.id,
                            document.get("name") as String,
                            document.get("surname") as String,
                            document.get("email") as String
                        )
                    )
                }

                onSuccess(tempList)
            }

            error?.let { onFailure(it.message.orEmpty()) }
        }
    }

    fun searchDocument(
        documentId: String,
        onSuccess: (Contact) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.document(documentId).get().addOnSuccessListener { document ->
            onSuccess(
                Contact(
                    document.id,
                    document.get("name") as String,
                    document.get("surname") as String,
                    document.get("email") as String
                )
            )

        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun deleteData(
        documentId: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.document(documentId).delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }
}