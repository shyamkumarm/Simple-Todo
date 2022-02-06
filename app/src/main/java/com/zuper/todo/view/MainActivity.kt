package com.zuper.todo.view


import android.content.Intent
import android.content.res.Configuration
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.zuper.todo.BaseActivity
import com.zuper.todo.R
import com.zuper.todo.databinding.ActivityMainBinding
import com.zuper.todo.model.Data
import com.zuper.todo.model.PriorityModel
import com.zuper.todo.network.response.Resource
import com.zuper.todo.network.response.Status
import com.zuper.todo.utils.ApiConstants
import com.zuper.todo.view.adapter.PriorityAdapter
import com.zuper.todo.view.adapter.ViewAdapter
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private lateinit var viewAdapter: ViewAdapter
    private lateinit var priorityAdapter: PriorityAdapter

   private var selectedItem: PriorityModel? =null
   private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? =null


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getContentLayout(): View = binding.root
    override fun onCreate() {

        initView()
        initObserver()


    }

    private fun initView() {
        setSupportActionBar(binding.toolbar.apply { title = "${ApiConstants.AUTHOR_NAME}'s todo" })
        ViewCompat.setTranslationZ(binding.mainContent.progress, 5f)
        binding.mainContent.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
           viewAdapter =  ViewAdapter { pos, data ->
               viewModel.markCompletedOrNot(data)
           }
            adapter = viewAdapter

        }
        priorityAdapter = PriorityAdapter(this)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.designBottomSheet.root)
        binding.designBottomSheet.spinner.adapter = priorityAdapter

        binding.designBottomSheet.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (position >= 0) {
                        selectedItem =
                            parent.getItemAtPosition(position) as PriorityModel
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.mainContent.outlinedTextField.editText?.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && v.text.isNotBlank()) {
                clearFocusKeyboard()
                showToast("Searching for ${v.text}")
                if (v.text.toString() == ApiConstants.TAG_ALL) getTodos()
                else getTodos(v.text.toString())
            }
            true
        }

        binding.designBottomSheet.button.setOnClickListener {
            binding.designBottomSheet.let {
                if(it.taskString.text.toString().isBlank() ||it.taskString.text.toString().isEmpty()){
                    it.taskString.error = getString(R.string.enter_task_name)
                }
               else if(it.tagString.text.toString().isBlank() ||it.tagString.text.toString().isEmpty()){
                    it.tagString.error = getString(R.string.enter_tag_name)
                }else
                viewModel.createTodo(it.taskString.text.toString(),it.tagString.text.toString(),selectedItem?.priorityType,viewAdapter.getLastId())
            }

        }
        getTodos()
    }

    private fun getTodos(tagName:String? =null) {
        viewModel.getPagingTodo(ApiConstants.AUTHOR_NAME,tagName,ApiConstants.DEFAULT_PAGE_SIZE).observe(this, {
            lifecycleScope.launch {
                viewAdapter.submitData(it)
            }
        })
    }

    private fun initObserver() {

       initCallback()

       viewModel.markedTodo.observe(this, {
           when (it.status) {
               Status.SUCCESS -> {
                   viewAdapter.notifyMarkedChanges()
               }
               else -> loadingStatus(it)
           }
       })

       viewModel.createdTodo.observe(this, {
           when (it.status) {
               Status.SUCCESS -> {
                       viewAdapter.refresh()
                       binding.designBottomSheet.let { sheetView ->
                           sheetView.taskString.text?.clear()
                           sheetView.tagString.text?.clear()
                           showToast(getString(R.string.create_success), Toast.LENGTH_LONG)
                         //  bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                       }
                       clearFocusKeyboard()
                      // getContentLayout().mainContent.recyclerView.scrollToPosition(viewAdapter.itemCount-1)
               }
                else -> loadingStatus(it)
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
                    binding.mainContent.progress.visibility = View.GONE
                    binding.mainContent.recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun loadingStatus(status: Resource<Data>) {

        if(Status.LOADING ==status.status) {
            binding.mainContent.progress.visibility = View.VISIBLE
        }
        else {
            //Handle Error
            binding.mainContent.progress.visibility = View.GONE
            showToast(status.message, Toast.LENGTH_LONG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
       menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_tag_view){

            Intent(this,TagViewActivity::class.java).run {
                startActivity(this)
            }
        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
    }




}


