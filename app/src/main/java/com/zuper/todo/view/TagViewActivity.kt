package com.zuper.todo.view

import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zuper.todo.BaseActivity
import com.zuper.todo.databinding.ActivityTagViewBinding
import com.zuper.todo.model.Data
import com.zuper.todo.network.response.Resource
import com.zuper.todo.network.response.Status
import com.zuper.todo.utils.ApiConstants
import com.zuper.todo.view.adapter.TagViewAdapter
import kotlinx.coroutines.launch

class TagViewActivity : BaseActivity() {

    private lateinit var viewAdapter: TagViewAdapter
    private val tagViewBinding  by lazy {
        ActivityTagViewBinding.inflate(layoutInflater)
    }
    override fun getContentLayout() = tagViewBinding.root
    override fun onCreate() {
        initView()
        initCallback()
        getTodos()
    }

    private fun initView() {
        setSupportActionBar(tagViewBinding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        ViewCompat.setTranslationZ(tagViewBinding.progress, 5f)
        tagViewBinding.tagRecylerView.apply {
            layoutManager = LinearLayoutManager(context)
            viewAdapter = TagViewAdapter()
            adapter = viewAdapter

        }
    }

    private fun getTodos() {
        viewModel.getPagingTagTodo(ApiConstants.AUTHOR_NAME,null,ApiConstants.MAX_PAGE_SIZE).observe(this, {
            lifecycleScope.launch {
                viewAdapter.submitData(it)
            }
        })
    }

    private fun initCallback() {
        viewAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Error ->
                    loadingStatus(
                        Resource.error(
                            (it.refresh as LoadState.Error).error.message ?: "", null
                        )
                    )
                is LoadState.Loading -> loadingStatus(Resource.loading(null))
                else -> { // success
                    tagViewBinding.progress.visibility = View.GONE
                }
            }
        }
    }

    private fun loadingStatus(status: Resource<Data>) {

        if(Status.LOADING ==status.status) {
            tagViewBinding.progress.visibility = View.VISIBLE
        }
        else {
            //Handle Error
            tagViewBinding.progress.visibility = View.GONE
            showToast(status.message, Toast.LENGTH_LONG)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}