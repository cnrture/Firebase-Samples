package com.canerture.firebaseexamples.common

import com.canerture.firebaseexamples.common.Constants.PRIORITY_HIGH
import com.canerture.firebaseexamples.common.Constants.PRIORITY_LOW
import com.canerture.firebaseexamples.common.Constants.PRIORITY_MEDIUM
import com.canerture.firebaseexamples.data.model.Todo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class FirestoreOperationsWrapper @Inject constructor(firestore: FirebaseFirestore) {

    private val collection = firestore.collection("Todos")

    fun addTodo(
        todo: String,
        priority: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val todoModel = hashMapOf(
            "todo" to todo,
            "priority" to priority,
            "isDone" to false,
            "date" to getDateTimeAsFormattedString()
        )

        collection.add(todoModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun setTodo(
        todo: String,
        priority: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val todoModel = hashMapOf(
            "todo" to todo,
            "priority" to priority,
            "isDone" to false,
            "date" to getDateTimeAsFormattedString()
        )

        collection.document(todo).set(todoModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun updateTodo(
        todo: String,
        priority: String,
        documentId: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.document(documentId).update(
            mapOf(
                "todo" to todo,
                "priority" to priority
            )
        )
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun updateDoneState(
        isDone: Boolean,
        documentId: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.document(documentId).update("isDone", isDone)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun getTodosOnce(
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.get().addOnSuccessListener {

            val tempList = arrayListOf<Todo>()

            it.forEach { document ->
                tempList.add(
                    Todo(
                        document.id,
                        document.get("todo") as String,
                        document.get("priority") as String,
                        document.get("isDone") as Boolean,
                        document.get("date") as String
                    )
                )
            }

            onSuccess(tempList)
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun getTodosRealtime(
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.addSnapshotListener { snapshot, error ->

            val tempList = arrayListOf<Todo>()

            snapshot?.let {
                it.forEach { document ->
                    tempList.add(
                        Todo(
                            document.id,
                            document.get("todo") as String,
                            document.get("priority") as String,
                            document.get("isDone") as Boolean,
                            document.get("date") as String
                        )
                    )
                }

                onSuccess(tempList)
            }

            error?.let { onFailure(it.message.orEmpty()) }
        }
    }

    fun getDoneTodosRealtime(
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.whereEqualTo("isDone", true).addSnapshotListener { snapshot, error ->

            val tempList = arrayListOf<Todo>()

            snapshot?.let {
                it.forEach { document ->
                    tempList.add(
                        Todo(
                            document.id,
                            document.get("todo") as String,
                            document.get("priority") as String,
                            document.get("isDone") as Boolean,
                            document.get("date") as String
                        )
                    )
                }

                onSuccess(tempList)
            }

            error?.let { onFailure(it.message.orEmpty()) }
        }
    }

    fun getNotDoneTodosRealtime(
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.whereEqualTo("isDone", false).orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onFailure(error.message.orEmpty())
                    return@addSnapshotListener
                }

                val tempList = arrayListOf<Todo>()

                snapshot?.let {
                    it.forEach { document ->
                        tempList.add(
                            Todo(
                                document.id,
                                document.get("todo") as String,
                                document.get("priority") as String,
                                document.get("isDone") as Boolean,
                                document.get("date") as String
                            )
                        )
                    }

                    onSuccess(tempList)
                }
            }
    }

    fun getTodoByDocumentIdOnce(
        documentId: String,
        onSuccess: (Todo) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.document(documentId).get().addOnSuccessListener { document ->
            onSuccess(
                Todo(
                    document.id,
                    document.get("todo") as String,
                    document.get("priority") as String,
                    document.get("isDone") as Boolean,
                    document.get("date") as String
                )
            )

        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun deleteTodo(
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

    fun queryTodo(
        query: String,
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.whereEqualTo("todo", query).get().addOnSuccessListener { documents ->

            val tempList = arrayListOf<Todo>()

            documents?.let {
                it.forEach { document ->
                    tempList.add(
                        Todo(
                            document.id,
                            document.get("todo") as String,
                            document.get("priority") as String,
                            document.get("isDone") as Boolean,
                            document.get("date") as String
                        )
                    )
                }

                onSuccess(tempList)
            }
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun getTodoByPriorityOnce(
        priority: String,
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.whereEqualTo("priority", priority).get().addOnSuccessListener { documents ->

            val tempList = arrayListOf<Todo>()

            documents?.let {
                it.forEach { document ->
                    tempList.add(
                        Todo(
                            document.id,
                            document.get("todo") as String,
                            document.get("priority") as String,
                            document.get("isDone") as Boolean,
                            document.get("date") as String
                        )
                    )
                }

                onSuccess(tempList)
            }
        }.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun getStatistics(
        statistics: (Int, Int, Int, Int, Int) -> Unit = { _, _, _, _, _ -> }
    ) {

        getTodosOnce({ todoList ->
            val done = todoList.filter { it.isDone }.size
            val notDone = todoList.filter { !it.isDone }.size
            val lowPriority = todoList.filter { it.priority == PRIORITY_LOW }.size
            val mediumPriority = todoList.filter { it.priority == PRIORITY_MEDIUM }.size
            val highPriority = todoList.filter { it.priority == PRIORITY_HIGH }.size

            statistics(done, notDone, lowPriority, mediumPriority, highPriority)
        })
    }
}