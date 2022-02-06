package com.zuper.todo.viewmodels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.zuper.todo.model.Data
import com.zuper.todo.model.ShowToDo
import com.zuper.todo.model.TagUiModel
import com.zuper.todo.network.request.TodoClientRepo
import com.zuper.todo.network.response.Resource
import com.zuper.todo.paging.PagingRepo


class TodoViewModel (private val eventRequestCall: TodoClientRepo, private val pagingRepo: PagingRepo) : ViewModel() {

    var todoList = MutableLiveData<Resource<ShowToDo>>()
    var createdTodo = MutableLiveData<Resource<Data>>()
    var markedTodo = MutableLiveData<Resource<Data>>()

/*    fun getTodo() =
        eventRequestCall.getReqQuery("1","15","Ranjith",todoList)*/

    fun markCompletedOrNot(item:Data) =
        eventRequestCall.markStatus(item,markedTodo)

     fun createTodo(task: String?, tagString: String?, priorityType: String?, lastId: Int) {
        eventRequestCall.createTodo(task,"Ranjith",tagString,priorityType,createdTodo,lastId)
    }


    fun getPagingTodo(authorName:String,tagName:String? =null,pageSize: Int): LiveData<PagingData<Data>> {
        return pagingRepo.getPagingTagTodoReq(authorName,tagName,pageSize)
            //.map { it -> it.map { it.data } }
            .cachedIn(viewModelScope)
    }

    fun getPagingTagTodo(authorName:String,tagName:String? =null,pageSize: Int): LiveData<PagingData<TagUiModel>> {
        return pagingRepo.getPagingTagTodoReq(authorName,tagName,pageSize)
            //.map { it -> it.map { it.data } }
            .map { pagingData: PagingData<Data> ->
            // Map outer stream, so you can perform transformations on
            // each paging generation.
            pagingData
                .map { data ->
                    // Convert items in stream to UiModel.UserModel.
                    TagUiModel.TaskModel(data)
                }
                .insertSeparators { before, after ->
                    if (before == null && after == null) {
                        // List is empty after fully loaded; return null to skip adding separator.
                        null
                    } else if (after == null) {
                        // Footer; return null here to skip adding a footer.
                        null
                    } else if (before == null) {
                        // Header
                        TagUiModel.SeparatorBadgeModel(after.tag)
                    } else if ((before.tag != after.tag)){
                        // Between two items that start with different letters.
                        TagUiModel.SeparatorBadgeModel(after.tag)
                    } else {
                        // Between two items that start with the same letter.
                        null
                    }
                }
        }.cachedIn(viewModelScope)

    }
}