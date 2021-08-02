package com.prabhakaran.roomdbexample.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.prabhakaran.roomdbexample.R
import com.prabhakaran.roomdbexample.adapter.BorrowedToolsListAdapter
import com.prabhakaran.roomdbexample.appUtils.Constants
import com.prabhakaran.roomdbexample.dB.AppDb
import com.prabhakaran.roomdbexample.listener.BorrowedToolsListListener
import com.prabhakaran.roomdbexample.model.BorrowedToolsDetails
import com.prabhakaran.roomdbexample.model.FriendsDetails
import kotlinx.android.synthetic.main.activity_friends_borrowed_tools_details.*


/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
class FriendsBorrowedToolsDetailsActivity : AppCompatActivity(), BorrowedToolsListListener {
    private var borrowedToolsList: ArrayList<BorrowedToolsDetails> = ArrayList()
    private var borrowedToolsDetails: FriendsDetails? = null
    private var borrowedToolsListAdapter: BorrowedToolsListAdapter? = null
    private var dialog: Dialog? = null
    private var appDb: AppDb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends_borrowed_tools_details)
        initViews()
    }

    @SuppressLint("WrongConstant")
    private fun initViews() {
        //initialize the db
        appDb = AppDb.getAppDB(this)
        setActionBar()
        if (intent != null) {
            //get loaned details for showing the data in the page
            borrowedToolsDetails = appDb?.friendDao()?.getFriendDetails(intent.getStringExtra(Constants.SELECTED_FRIEND_NAME))
            textFriendName.text = borrowedToolsDetails?.friendName

        }
        if (borrowedToolsDetails?.borrowedToolsList!!.isNotEmpty()) {
            borrowedToolsDetails!!.borrowedToolsList?.let { borrowedToolsList.addAll(it) }
            val iterate = borrowedToolsList.iterator()
            while (iterate.hasNext()) {
                val value = iterate.next()
                if (value.borrowedItem == 0) {
                    iterate.remove()
                }
            }
            borrowedToolsListAdapter = BorrowedToolsListAdapter(borrowedToolsList, this)
            rvToolsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvToolsList.adapter = borrowedToolsListAdapter
            borrowedToolsListAdapter!!.setBorrowedToolsListListener(this)

        }
        listVisibility()

    }

    private fun listVisibility() {
        if (borrowedToolsList.isNotEmpty()) {
            textNoDataFound.visibility = View.GONE
            rvToolsList.visibility = View.VISIBLE
        } else {
            textNoDataFound.visibility = View.VISIBLE
            rvToolsList.visibility = View.GONE
        }

    }

    private fun setActionBar() {
        supportActionBar?.title = "Tools Details"
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun itemReturn(position: Int) {
        showDialogWindow(position)

    }

    @SuppressLint("WrongConstant")
    private fun showDialogWindow(position: Int) {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_return_tool)
        val textCancel = dialog!!.findViewById(R.id.textCancel) as TextView
        val textOk = dialog!!.findViewById(R.id.textOk) as TextView
        textCancel.setOnClickListener {
            dialog!!.dismiss()
        }
        textOk.setOnClickListener {
            dialog!!.dismiss()
            //get selected tools details from db
            val toolDetails = appDb!!.toolsDao().getToolsDetails(borrowedToolsList[position].name)
            if (toolDetails.borrowedItem != 0) {
                appDb!!.toolsDao().updateBorrowedCount(
                    toolDetails.borrowedItem - 1,
                    borrowedToolsList[position].name
                )
            }
            //get loaned details from db
            val friendsDetails = appDb?.friendDao()?.getFriendDetails(borrowedToolsDetails!!.friendName)
            //delete existing details of loaned details
            friendsDetails?.let { appDb?.friendDao()!!.deleteFriendDetails(it) }
            val borrowedList = ArrayList<BorrowedToolsDetails>()
            if (friendsDetails != null) {
                friendsDetails.borrowedToolsList?.let { borrowedList.addAll(it) }
            }

            if (borrowedList.size > 0 && borrowedList[position].borrowedItem!=0) {
                borrowedList[position].borrowedItem = borrowedList[position].borrowedItem - 1
            }
            if (friendsDetails != null) {
                friendsDetails.borrowedToolsList = borrowedList
            }

            //insert friend's loaned tools details to db
            friendsDetails?.let { appDb?.friendDao()?.insertFriend(it) }
            if(borrowedToolsList[position].borrowedItem!=0)
            borrowedToolsList[position].borrowedItem = borrowedToolsList[position].borrowedItem - 1
            if (borrowedToolsList[position].borrowedItem == 0)
                borrowedToolsList.removeAt(position)
            borrowedToolsListAdapter!!.notifyDataSetChanged()
            listVisibility()
            Toast.makeText(this, borrowedToolsDetails!!.friendName + " returned this tool.", Toast.LENGTH_LONG).show()

        }
        dialog!!.show()

    }
}
