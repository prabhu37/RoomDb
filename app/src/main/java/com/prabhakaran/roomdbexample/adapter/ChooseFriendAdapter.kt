package com.prabhakaran.roomdbexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.roomdbexample.listener.ChooserFriendListener
import com.prabhakaran.roomdbexample.R
import kotlinx.android.synthetic.main.item_choose_friends.view.*

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
class ChooseFriendAdapter(var friends: ArrayList<String>, var context: Context) :
    RecyclerView.Adapter<ChooseFriendAdapter.ViewHolder>() {

    private var chooserFriendListener: ChooserFriendListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choose_friends, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend: String = friends[position]
        chooserFriendListener?.let { holder.bind(friend, it, position) }

    }

    override fun getItemCount(): Int {
        return friends.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            friend: String,
            chooserFriendListener: ChooserFriendListener,
            position: Int
        ) {
            itemView.textFriendName.text = friend
            itemView.itemChooseFriend.setOnClickListener {
                chooserFriendListener.selectFriend(position)
            }

        }
    }

    fun setOnChooseFriendListListener(chooserFriendListener: ChooserFriendListener) {
        this.chooserFriendListener = chooserFriendListener
    }

}