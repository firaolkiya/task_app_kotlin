package com.example.learn.data.repository_impl

import com.example.learn.domain.entities.TaskEntity
import com.example.learn.domain.repository.TaskRepository
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject

class TaskRepositoryImpl : TaskRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private  var listenerRegistration: ListenerRegistration? = null

    override fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, TaskEntity) -> Unit,
        onError: (Throwable) -> Unit
    ) {

        val query = firestore
            .collection("task_list")
            .whereEqualTo("userId", userId)
         listenerRegistration =  query.addSnapshotListener{value,error->
            if (error!=null){
                onError(error)
                return@addSnapshotListener
            }
            value?.documentChanges?.forEach{
                val wasDeleted = it.type==DocumentChange.Type.REMOVED
                val tasks = it.document.toObject<TaskEntity>().copy(id = it.document.id)
                onDocumentEvent(wasDeleted,tasks)
            }

        }



    }

    override fun removeListener() {
        listenerRegistration?.remove()
    }

    override fun getTask(
        taskId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (TaskEntity) -> Unit
    ) {
        firestore.collection("task_list")
            .whereEqualTo("id",taskId)
            .get()
            .addOnSuccessListener { querySnapshot->
                if (!querySnapshot.isEmpty) {
                    // Assuming TaskEntity is a data class
                    val document = querySnapshot.documents.first() // Get the first matching document
                    val task = document.toObject(TaskEntity::class.java) // Convert to TaskEntity
                    if (task != null) {
                        onSuccess(task) // Call success callback
                    } else {
                        onError(Throwable("Task not found or data mismatch"))
                    }
                } else {
                    onError(Throwable("No task found with the provided ID"))
                }
            }
            .addOnFailureListener{
                onError(it)
            }


    }

    override fun saveTask(task: TaskEntity, onError: (Throwable?) -> Unit) {
        firestore.collection("task_list")
            .add(task)
            .addOnCompleteListener {
                onError(it.exception)
            }
    }

    override fun updateTask(task: TaskEntity, onError: (Throwable?) -> Unit) {
        firestore.collection("task_list")
            .document(task.id)
            .set(task)
            .addOnCompleteListener {
                onError(it.exception)
            }
    }

    override fun deleteTask(taskId: String, onError: (Throwable?) -> Unit) {
        firestore.collection("task_list")
            .document(taskId)
            .delete()
            .addOnCompleteListener {
                onError(it.exception)
            }
    }
}