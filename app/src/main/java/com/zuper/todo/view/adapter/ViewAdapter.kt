package com.zuper.todo.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuper.todo.R
import com.zuper.todo.databinding.ItemLayoutBinding
import com.zuper.todo.model.Data
import com.zuper.todo.utils.ApiConstants


class ViewAdapter(
    val callback:(Int,Data)->Unit
) : PagingDataAdapter<Data, ViewAdapter.ViewHolder>(diffCallback) {

    private val TAG = "ViewAdapter"


    fun getLastId():Int = getItem(itemCount-1)?.id?.plus(1) ?: 1
    fun getLastPosition():Int = itemCount-1
    var getLastClickedPos:Int = 0




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

      return  ViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        Log.d(TAG,item.toString())
        holder.mView.root.setOnClickListener {
            getLastClickedPos = holder.absoluteAdapterPosition
            callback(
                holder.absoluteAdapterPosition,
                getItem(holder.absoluteAdapterPosition)!!
            )
        }

        holder.mView.apply {
                titleView.text = item?.title
                tagView.text = item?.tag
          val color = when(item?.priority){
                ApiConstants.LOW ->ContextCompat.getColor(root.context, R.color.green)
                ApiConstants.MEDIUM ->ContextCompat.getColor(root.context, R.color.yellow)
                else-> ContextCompat.getColor(root.context, R.color.red)
            }
            imageBannerView.setColorFilter(color)
            if(item?.isCompleted == true)checkbox.alpha = 1.0f
            else checkbox.alpha = 0.0f

        }


    }

    fun notifyMarkedChanges() {
    notifyItemChanged(getLastClickedPos)
    }

    fun notifyAddedChanges() {
        notifyItemInserted(getLastPosition())

    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
                 oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(val mView: ItemLayoutBinding) : RecyclerView.ViewHolder(mView.root)


}



