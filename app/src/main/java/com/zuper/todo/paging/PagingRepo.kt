package com.zuper.todo.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.zuper.todo.model.Data
import org.koin.java.KoinJavaComponent.getKoin

class PagingRepo {

    private fun getDefaultPageConfig(pageSize:Int): PagingConfig {
        return PagingConfig(pageSize = pageSize, enablePlaceholders = true)
    }

    private fun getPagingDataSrc():PagingDataSource = getKoin().get(PagingDataSource::class)




    fun getPagingTagTodoReq(authorName:String,tagName:String?=null,pageSize: Int): LiveData<PagingData<Data>> {
        return Pager(
            config = getDefaultPageConfig(pageSize),
            pagingSourceFactory = { getPagingDataSrc().apply { setAuthorName = authorName
                setTagName = tagName} }
        ).liveData
    }

}