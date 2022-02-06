package com.zuper.todo.paging

import androidx.paging.PagingSource
import com.zuper.todo.model.Data
import com.zuper.todo.network.ResponseApi
import com.zuper.todo.utils.ApiConstants
import com.zuper.todo.utils.SystemUtils
import retrofit2.HttpException
import java.io.IOException

class PagingDataSource(private val systemUtils: SystemUtils,private val eventApi: ResponseApi) :
    PagingSource<Int, Data>() {

    var setTagName:String? =null
    lateinit var setAuthorName:String
    /**
     * calls api if there is any error getting results then return the [LoadResult.Error]
     * for successful response return the results using [LoadResult.Page] for some reason if the results
     * are empty from service like in case of no more data from api then we can pass [null] to
     * send signal that source has reached the end of list
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        //for first case it will be null, then we can pass some default value, in our case it's 1
        if (systemUtils.isNetworkConnected()) {
            val page = params.key ?: ApiConstants.DEFAULT_PAGE_INDEX
            return try {
                val response =
                    eventApi.getRequestPagingDataAsync(page, params.loadSize, setAuthorName,setTagName).await()
                if (response.isSuccessful) {
                    response.body()?.data?.sortByDescending{it.tag}
                    LoadResult.Page(
                        response.body()?.data?.reversed()!!,
                        prevKey = if (page == ApiConstants.DEFAULT_PAGE_INDEX) null else page - 1,
                        nextKey = if (response.body()?.data.isNullOrEmpty()) null else page + 1
                    )
                }else LoadResult.Error(Exception(response.errorBody()?.string()))
            } catch (exception: IOException) {
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                return LoadResult.Error(exception)
            }
        }else return LoadResult.Error(Exception(ApiConstants.NO_INTERNET))
    }


}