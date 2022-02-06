package com.zuper.todo.network

import com.zuper.todo.model.Data
import com.zuper.todo.model.ShowToDo
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface ResponseApi {

    // not for paging
    @GET("todo?")
    fun getRequestDataAsync(@Query("_page") location: String,
                            @Query("_limit") portalId: String ="",@Query("author") author: String =""): Deferred<Response<ShowToDo>>


    @POST("todo")
    fun createRequestAsync(@Body login: Data?):  Deferred<Response<Data>>

    @PUT("todo/{id}")
     fun markStatusAsync(@Body login: Data?,@Path("id")id:Int): Deferred<Response<Data>>





     // paging
    @GET("todo?")
    fun getRequestPagingDataAsync(@Query("_page") location: Int,
                                  @Query("_limit") portalId: Int,@Query("author") author: String ="",@Query("tag") tag: String?): Deferred<Response<ShowToDo>>
}