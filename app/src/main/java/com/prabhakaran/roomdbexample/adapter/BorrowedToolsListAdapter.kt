package com.prabhakaran.roomdbexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.roomdbexample.listener.BorrowedToolsListListener
import com.prabhakaran.roomdbexample.model.BorrowedToolsDetails
import com.prabhakaran.roomdbexample.R
import kotlinx.android.synthetic.main.item_borrowed_tools.view.*
import kotlinx.android.synthetic.main.item_tools.view.imageTools
import kotlinx.android.synthetic.main.item_tools.view.textBorrowedCount
import kotlinx.android.synthetic.main.item_tools.view.textToolName

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
class BorrowedToolsListAdapter(var borrowedToolList: ArrayList<BorrowedToolsDetails>, var context: Context) :
    RecyclerView.Adapter<BorrowedToolsListAdapter.ToolsListViewModel>() {
    private var borrowedToolsListListener: BorrowedToolsListListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolsListViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_borrowed_tools, parent, false)
        return ToolsListViewModel(view)
    }

    override fun onBindViewHolder(holder: ToolsListViewModel, position: Int) {
        val borrowedToolsDetails: BorrowedToolsDetails = borrowedToolList[position]
        borrowedToolsListListener?.let { holder.bind(borrowedToolsDetails, it, position) }
    }

    override fun getItemCount(): Int {
        return borrowedToolList.size
    }

    class ToolsListViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            borrowedToolList: BorrowedToolsDetails,
            borrowedToolsListListener: BorrowedToolsListListener,
            position: Int
        ) {
            itemView.textToolName.text = borrowedToolList.name
            itemView.imageTools.setImageResource(borrowedToolList.imageId)
            itemView.textBorrowedCount.text = borrowedToolList.borrowedItem.toString()
            itemView.itemBorrowedTools.setOnClickListener {
                borrowedToolsListListener.itemReturn(position)
            }

        }
    }

    fun setBorrowedToolsListListener(borrowedToolsListListener: BorrowedToolsListListener) {
        this.borrowedToolsListListener = borrowedToolsListListener
    }

}