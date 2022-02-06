package com.zuper.todo.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuper.todo.R
import com.zuper.todo.databinding.BadgeViewBinding
import com.zuper.todo.databinding.ItemWithoutTagBinding
import com.zuper.todo.model.TagUiModel
import com.zuper.todo.utils.ApiConstants

class TagViewAdapter : PagingDataAdapter<TagUiModel, RecyclerView.ViewHolder>(diffCallback) {



    private val TAG: String = "TagViewAdapter"

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ApiConstants.BADGE_VIEW -> BadgeHolder(
                BadgeViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> TagViewHolder(
                ItemWithoutTagBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TagUiModel.TaskModel -> ApiConstants.TAG_VIEW
            is TagUiModel.SeparatorBadgeModel -> ApiConstants.BADGE_VIEW
            null -> throw IllegalStateException("Unknown view")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)


        if(holder is BadgeHolder) {
            holder.mView.tagView.text =
                (item as TagUiModel.SeparatorBadgeModel).badgeText
            Log.d(TAG, item.toString())
        }
       if(holder is TagViewHolder) {
            holder.mView.apply {
                titleView.text = (item as TagUiModel.TaskModel).title
                val color = when (item.priority) {
                    ApiConstants.LOW -> ContextCompat.getColor(root.context, R.color.green)
                    ApiConstants.MEDIUM -> ContextCompat.getColor(root.context, R.color.yellow)
                    else -> ContextCompat.getColor(root.context, R.color.red)
                }
                priorityBadge.setColorFilter(color)
            }
           Log.d(TAG, item.toString())
        }

    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<TagUiModel>() {
            override fun areItemsTheSame(oldItem: TagUiModel, newItem: TagUiModel): Boolean {
                val isSameRepoItem = oldItem is TagUiModel.TaskModel
                        && newItem is TagUiModel.TaskModel
                        && oldItem.id == newItem.id

                val isSameSeparatorItem = oldItem is TagUiModel.SeparatorBadgeModel
                        && newItem is TagUiModel.SeparatorBadgeModel
                        && oldItem.badgeText == newItem.badgeText


                return isSameRepoItem || isSameSeparatorItem
            }

            override fun areContentsTheSame(
                oldItem: TagUiModel,
                newItem: TagUiModel
            ) = oldItem == newItem
        }
    }

    inner class TagViewHolder(val mView: ItemWithoutTagBinding) :
        RecyclerView.ViewHolder(mView.root)

    inner class BadgeHolder(val mView: BadgeViewBinding) : RecyclerView.ViewHolder(mView.root)


}