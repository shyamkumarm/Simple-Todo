package com.zuper.todo

import android.app.Application
import com.zuper.todo.modules.appModules
import com.zuper.todo.modules.networkModules
import com.zuper.todo.modules.viewModules

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()

         startKoin {
             androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(networkModules + viewModules + appModules)
        }
    }
}