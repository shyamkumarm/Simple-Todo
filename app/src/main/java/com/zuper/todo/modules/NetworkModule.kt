package com.zuper.todo.modules


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.zuper.todo.network.ResponseApi
import com.zuper.todo.network.request.TodoClientRepo
import com.zuper.todo.paging.PagingDataSource
import com.zuper.todo.paging.PagingRepo
import com.zuper.todo.utils.ApiConstants

import okhttp3.Interceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val sLogLevel =
    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE


private fun getLogInterceptor() = HttpLoggingInterceptor().apply { level = sLogLevel }

fun createNetworkClient() =
    retrofitClient(okHttpClient())

private fun okHttpClient() = OkHttpClient.Builder()
    .addInterceptor(getLogInterceptor()).apply { setTimeOutToOkHttpClient(this) }
    /* .addInterceptor(headersInterceptor(true))*/.build()


fun headersInterceptor(addAuthHeader: Boolean) = Interceptor { chain ->
    chain.proceed(
        chain.request().newBuilder()
            .addHeader(ApiConstants.CONTENT_TYPE, ApiConstants.APPLICATION_JSON)
            .also {
               /* if (addAuthHeader) {
                   it.addHeader(ApiConstants.AUTHORIZATION,ApiConstants.BEARER+ApiConstants.API_KEY)
                }*/
            }
            .build()
    )
}

private fun retrofitClient(httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .client(httpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


private fun setTimeOutToOkHttpClient(okHttpClientBuilder: OkHttpClient.Builder) =
    okHttpClientBuilder.apply {
        readTimeout(30L, TimeUnit.SECONDS)
        connectTimeout(30L, TimeUnit.SECONDS)
        writeTimeout(30L, TimeUnit.SECONDS)
    }


private val retrofit: Retrofit = createNetworkClient()
private val IMAGES_API: ResponseApi = retrofit.create(ResponseApi::class.java)

val networkModule = module {
    single { IMAGES_API }
    single { TodoClientRepo(get(), get()) }
    factory{ PagingDataSource(get(),get()) }
    single { PagingRepo() }
}

val networkModules = listOf(networkModule)