package com.prabhakaran.roomdbexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.roomdbexample.model.ToolsDetails
import kotlinx.android.synthetic.main.item_tools.view.*
import com.prabhakaran.roomdbexample.listener.ToolsListListeners
import com.prabhakaran.roomdbexample.R


/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
class ToolsListAdapter(var toolsList: ArrayList<ToolsDetails>, var context: Context) :
    RecyclerView.Adapter<ToolsListAdapter.ToolsListViewHolder>() {
    private var toolsListListeners: ToolsListListeners? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolsListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tools, parent, false)
        return ToolsListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolsListViewHolder, position:Int) {
        val toolsDetails: ToolsDetails = toolsList[position]
        toolsListListeners?.let { holder.bind(toolsDetails, it,position,context) }
    }

    override fun getItemCount(): Int {
        return toolsList.size
    }

    class ToolsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(toolsDetails: ToolsDetails, toolsListListeners:ToolsListListeners,position:Int,context: Context) {
            itemView.textToolName.text = toolsDetails.name
            itemView.imageTools.setImageResource(toolsDetails.imageId)
            itemView.textTotalCount.text = toolsDetails.totalItem.toString()
            itemView.textBorrowedCount.text = toolsDetails.borrowedItem.toString()
            itemView.itemParent.setOnClickListener {
                if(toolsDetails.totalItem!=toolsDetails.borrowedItem) {
                    toolsListListeners.chooseFriend(position)
                }else{
                    Toast.makeText(context,"No "+toolsDetails.name+" available",Toast.LENGTH_LONG).show()
                }
            }

        }

    }

    fun setOnToolsListListener(toolsListListeners: ToolsListListeners) {
        this.toolsListListeners = toolsListListeners
    }
}