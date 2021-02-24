package com.wuujcik.todolist.ui.details

import androidx.lifecycle.ViewModel
import com.wuujcik.todolist.model.TodoProvider
import com.wuujcik.todolist.persistence.Todo
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val todoProvider: TodoProvider) : ViewModel() {

   fun createItem(item: Todo){
      todoProvider.addItemToFirebase(item)
   }

   fun updateItem(item: Todo){
      todoProvider.updateItem(item)
      todoProvider.updateItemInFirebase(item)
   }
}
