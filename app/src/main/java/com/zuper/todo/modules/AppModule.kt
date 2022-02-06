package com.zuper.todo.modules

import com.zuper.todo.utils.SystemUtils
import org.koin.dsl.module

private val appModule = module {
    single { SystemUtils(get()) }

}




val appModules = listOf(appModule)