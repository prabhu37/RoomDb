package com.prabhakaran.roomdbexample.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.roomdbexample.activity.FriendsBorrowedToolsDetailsActivity
import com.prabhakaran.roomdbexample.appUtils.Constants
import com.prabhakaran.roomdbexample.model.FriendsDetails
import com.prabhakaran.roomdbexample.R
import kotlinx.android.synthetic.main.item_friends.view.*

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
class FriendsListAdapter(var friendList: ArrayList<FriendsDetails>, var context: Context) :
    RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {


    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val friendsDetails:FriendsDetails = friendList[position]
        holder.bindItems(friendsDetails,context)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friends, parent, false)
        return FriendsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendList.size

    }

    class FriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var context: Context? = null
        var friendsDetails: FriendsDetails? = null
        fun bindItems(friendsDetails: FriendsDetails,context: Context) {
            itemView.textFriendName.text=friendsDetails.friendName
            this.context = context
            this.friendsDetails = friendsDetails
            itemView.itemParent.setOnClickListener(clickListener)


        }

        private val clickListener: View.OnClickListener = View.OnClickListener { view ->
            when (view.id) {
               itemView.itemParent.id-> {
                   val intent = Intent(context, FriendsBorrowedToolsDetailsActivity::class.java)
                   intent.putExtra(Constants.SELECTED_FRIEND_NAME,friendsDetails!!.friendName)
                   context?.startActivity(intent)
                }

            }
        }

    }
}