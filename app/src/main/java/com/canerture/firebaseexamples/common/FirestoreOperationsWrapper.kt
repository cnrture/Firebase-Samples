package com.canerture.firebaseexamples.common

import com.canerture.firebaseexamples.data.model.Todo
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreOperationsWrapper @Inject constructor(firestore: FirebaseFirestore) {

    private val collection = firestore.collection("Todos")

    fun addTodo(
        todo: Todo,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val todoModel = hashMapOf(
            "todo" to todo.todo,
            "priority" to todo.priority,
            "isDone" to todo.isDone,
            "date" to getDateTimeAsFormattedString()
        )

        collection.add(todoModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun setTodo(
        todo: Todo,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        val todoModel = hashMapOf(
            "todo" to todo.todo,
            "priority" to todo.priority,
            "isDone" to todo.isDone,
            "date" to getDateTimeAsFormattedString()
        )

        collection.document(todo.todo ?: "Todo").set(todoModel)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun updateTodo(
        todo: String,
        documentId: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.document(documentId).update("todo", todo)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

    fun updateImportantState(
        priority: String,
        documentId: String,
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {
        collection.document(documentId).update("priority", priority)
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

    fun getTodosWithRealtimeUpdates(
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

    fun getDoneTodosWithRealtimeUpdates(
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

    fun getNotDoneTodosWithRealtimeUpdates(
        onSuccess: (List<Todo>) -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) {

        collection.whereEqualTo("isDone", false).addSnapshotListener { snapshot, error ->

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

    fun getTodoByDocumentId(
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

    fun queryTodoByPriority(
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
}