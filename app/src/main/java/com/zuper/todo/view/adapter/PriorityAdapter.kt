package com.zuper.todo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.zuper.todo.databinding.PiroritySpinnerListBinding
import com.zuper.todo.model.PriorityModel
import com.zuper.todo.utils.ApiConstants


class PriorityAdapter(
    context: Context,
    algorithmList: ArrayList<PriorityModel> = arrayListOf(PriorityModel(ContextCompat.getColor(context, com.zuper.todo.R.color.green),ApiConstants.LOW),
        PriorityModel(ContextCompat.getColor(context, com.zuper.todo.R.color.yellow),ApiConstants.MEDIUM),
        PriorityModel(ContextCompat.getColor(context, com.zuper.todo.R.color.red),ApiConstants.HIGH))
    ) : ArrayAdapter<PriorityModel>(context, 0, algorithmList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView,parent)
    }

        override fun getDropDownView(
            position: Int,
          convertView: View?,
            parent: ViewGroup
        ): View {
            return initView(position,convertView, parent)
        }

        private fun initView(
            position: Int,
            convertView:View?,
            parent: ViewGroup
        ): View {
           val  view  = PiroritySpinnerListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

            val currentItem: PriorityModel? = getItem(position)

            view.priorityText.text = currentItem?.priorityType
            view.priorityIcon.setColorFilter( currentItem?.drawableColor!!)

            return view.root
        }

}