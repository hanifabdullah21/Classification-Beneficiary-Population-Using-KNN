package com.singpaulee.klasifikasipenerimabantuan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.singpaulee.klasifikasipenerimabantuan.R
import com.singpaulee.klasifikasipenerimabantuan.model.ClassificationDataModel
import kotlinx.android.synthetic.main.itemview_classification.view.*

class ClassificationAdapter(var context: Context, var listData:ArrayList<ClassificationDataModel>) : RecyclerView.Adapter<ClassificationAdapter.ViewHolder>() {

    lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemView = LayoutInflater.from(context).inflate(R.layout.itemview_classification, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(classificationDataModel: ClassificationDataModel) {
            itemView.ic_tv_name.text = classificationDataModel.name
            itemView.ic_tv_status.text = classificationDataModel.status
        }
    }

}