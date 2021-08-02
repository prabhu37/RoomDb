package com.prabhakaran.roomdbexample.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.prabhakaran.roomdbexample.adapter.FriendsListAdapter
import com.prabhakaran.roomdbexample.dB.AppDb
import com.prabhakaran.roomdbexample.model.FriendsDetails
import com.prabhakaran.roomdbexample.R
import kotlinx.android.synthetic.main.fragment_friends.*

/**
 * Created by P.Prabhakaran on 09,November,2019
 **/
class FriendsFragment : Fragment() {

    private var mRootView: View? = null
    private var friendList: ArrayList<FriendsDetails> = ArrayList()
    private var friendsListAdapter: FriendsListAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_friends, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("WrongConstant")
    private fun initViews() {
        //Initialize the db
        val appDb: AppDb = context?.let { AppDb.getAppDB(it) }!!
        //check friend's details in db, add friend list to db when db list is empty
        if(appDb.friendDao().getCount()==0) {
            val friends = activity?.resources?.getStringArray(R.array.friends)
            for (i in friends?.indices!!) {
                val friendsDetails = FriendsDetails(i, friends[i].toString(), ArrayList())
                friendList.add(friendsDetails)
            }
            appDb.friendDao().insertAll(friendList)

        }
        //get all details of friends list in db
        friendList = appDb.friendDao().getAll() as ArrayList<FriendsDetails>
        friendsListAdapter = activity?.let { FriendsListAdapter(friendList, it) }
        rvFriendsList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvFriendsList.adapter = friendsListAdapter


    }


}