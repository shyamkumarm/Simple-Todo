package com.zuper.todo.utils

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.zuper.todo.model.Data

class ItemDiffCallback : DiffUtil.Callback() {
    private lateinit var oldList: List<Data>
    private lateinit var newList: List<Data>
    fun setList(oldList: List<Data>, newList: List<Data>){
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {


        return oldList[oldPosition].id == newList[newPosition].id
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}