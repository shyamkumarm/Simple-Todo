package com.zuper.todo.modules


import com.zuper.todo.viewmodels.TodoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module





val requestViewModel = module  {
    viewModel{ TodoViewModel(get(),get()) }

}

val viewModules = listOf(requestViewModel)

