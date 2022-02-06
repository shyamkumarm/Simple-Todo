package com.zuper.todo.network.request

import androidx.lifecycle.MutableLiveData
import com.zuper.todo.model.Data
import com.zuper.todo.model.ShowToDo
import com.zuper.todo.network.ResponseApi
import com.zuper.todo.network.response.Resource
import com.zuper.todo.utils.ApiConstants
import com.zuper.todo.utils.SystemUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class TodoClientRepo(
    private val systemUtils: SystemUtils,
    private val eventApi: ResponseApi
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    fun getReqQuery(
        page: String,limit:String,author :String,
        searchData: MutableLiveData<Resource<ShowToDo>>

    ) {
        launch {
            searchData.postValue(Resource.loading( null))
                if (systemUtils.isNetworkConnected()) {
                    val res = eventApi.getRequestDataAsync(page,limit,author).await()
                    if (res.isSuccessful) {
                        searchData.postValue(Resource.success(res.body()))
                    } else {
                        searchData.postValue(Resource.error(res.errorBody()?.string()?: ApiConstants.UN_DEFINED_ERROR , null))
                    }
                } else {
                    searchData.postValue(Resource.error(ApiConstants.NO_INTERNET, null))
                }
            }
        }

     fun createTodo(
         task: String?,
         author: String,
         tagString: String?,
         priorityType: String?,
         createdTodo: MutableLiveData<Resource<Data>>,
         lastId: Int
     ) {
         launch {
             if (systemUtils.isNetworkConnected()) {
                 val res = eventApi.createRequestAsync(
                     Data(
                        title =  task,
                        author =  author,
                         tag = tagString,
                         isCompleted = false,
                         priority = priorityType
                     )
                 ).await()
                 if (res.isSuccessful) {
                     createdTodo.postValue(Resource.success(res.body()))
                 } else {
                     createdTodo.postValue(
                         Resource.error(
                             res.errorBody()?.string() ?: ApiConstants.UN_DEFINED_ERROR, null
                         )
                     )
                 }
             } else {
                 createdTodo.postValue(Resource.error(ApiConstants.NO_INTERNET, null))
             }
         }
    }

    fun markStatus(item: Data, markedTodo: MutableLiveData<Resource<Data>>) {

        launch {
            if (systemUtils.isNetworkConnected()) {
                val res = eventApi.markStatusAsync(item.apply { isCompleted = isCompleted?.not()  ?: false}
                    ,item.id ?: 0
                ).await()
                if (res.isSuccessful) {
                    markedTodo.postValue(Resource.success(res.body()))
                } else {
                    markedTodo.postValue(
                        Resource.error(
                            res.errorBody()?.string() ?: ApiConstants.UN_DEFINED_ERROR, null
                        )
                    )
                }
            } else {
                markedTodo.postValue(Resource.error(ApiConstants.NO_INTERNET, null))
            }
        }

    }


}








