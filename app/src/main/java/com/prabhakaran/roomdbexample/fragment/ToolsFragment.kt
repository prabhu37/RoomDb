
package com.prabhakaran.roomdbexample.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.roomdbexample.adapter.ChooseFriendAdapter
import com.prabhakaran.roomdbexample.adapter.ToolsListAdapter
import com.prabhakaran.roomdbexample.listener.ChooserFriendListener
import com.prabhakaran.roomdbexample.listener.ToolsListListeners
import com.prabhakaran.roomdbexample.model.BorrowedToolsDetails
import com.prabhakaran.roomdbexample.dB.AppDb
import com.prabhakaran.roomdbexample.model.FriendsDetails
import com.prabhakaran.roomdbexample.model.ToolsDetails
import com.prabhakaran.roomdbexample.R
import kotlinx.android.synthetic.main.fragment_tools.*
import org.json.JSONObject
import java.io.InputStream

/**
 * Created by P.Prabhakaran on 09,November,2019
 **/
class ToolsFragment : Fragment(), ToolsListListeners, ChooserFriendListener {

    private var rootView: View? = null
    private var toolsList: ArrayList<ToolsDetails> = ArrayList()
    private var friendsList: ArrayList<String> = ArrayList()
    private var friendListDetails: ArrayList<FriendsDetails> = ArrayList()
    private var toolsListAdapter: ToolsListAdapter? = null
    private var dialog: Dialog? = null
    private var appDb: AppDb? = null
    private var selectedToolsPosition: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_tools, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    @SuppressLint("WrongConstant")
    private fun initViews() {
        //Initialize the db
        appDb = context?.let { AppDb.getAppDB(it) }!!
        //check tools's details in db, add tools list to db when db list is empty
        if (appDb!!.toolsDao().getCount() == 0) {
            val obj = JSONObject(readJSONFromAsset())
            val jsonArray = obj.getJSONArray("tools")
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val resourceId = resources.getIdentifier(item.getString("image"), "drawable", activity!!.packageName)
                val toolsDetails = ToolsDetails(i, item.getString("name"), item.getInt("quantity"), 0, resourceId)
                toolsList.add(toolsDetails)
            }
            appDb!!.toolsDao().insertAll(toolsList)

        }
        toolsList = appDb!!.toolsDao().getAll() as ArrayList<ToolsDetails>
        toolsListAdapter = activity?.let { ToolsListAdapter(toolsList, it) }
        rvToolsList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvToolsList.adapter = toolsListAdapter
        toolsListAdapter?.setOnToolsListListener(this)
        //check friend's details in db, add friend list to db when db list is empty
        if(appDb!!.friendDao().getCount()==0) {
            val friends = activity?.resources?.getStringArray(R.array.friends)
            for (i in friends?.indices!!) {
                val friendsDetails = FriendsDetails(i, friends[i].toString(),ArrayList())
                friendListDetails.add(friendsDetails)
            }
            appDb!!.friendDao().insertAll(friendListDetails)

        }


    }

    private fun readJSONFromAsset(): String? {
        val json: String?
        try {
            val inputStream: InputStream = activity?.assets!!.open("toolsdetails.json")
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override fun chooseFriend(position: Int) {
        selectedToolsPosition = position
        showDialog()
    }

    override fun selectFriend(position: Int) {
        dialog?.dismiss()
        //update the tools details in selected friend for loaned tool
        appDb?.toolsDao()?.update(toolsList[selectedToolsPosition].borrowedItem + 1, toolsList[selectedToolsPosition].name)
        toolsList[selectedToolsPosition].borrowedItem = toolsList[selectedToolsPosition].borrowedItem + 1
        toolsListAdapter?.notifyItemChanged(selectedToolsPosition)
        toolsListAdapter?.notifyDataSetChanged()
        Toast.makeText(activity, friendsList[position] + " borrowed this tool.", Toast.LENGTH_LONG).show()
       //get selected friend details from db
        val friendsDetails = appDb?.friendDao()?.getFriendDetails(friendsList[position])
        //delete existing data of selected friend in the db
        friendsDetails?.let { appDb?.friendDao()!!.deleteFriendDetails(it) }
        val borrowedList = ArrayList<BorrowedToolsDetails>()
        if (friendsDetails != null) {
            friendsDetails.borrowedToolsList?.let { borrowedList.addAll(it) }
        }
        var sameItemBorrowed = false
        //update loaned tool details in selected friend's details
        if (borrowedList.size > 0) {
            for (i in borrowedList.indices) {
                if (borrowedList[i].name.equals(toolsList[selectedToolsPosition].name)) {
                    borrowedList[i].borrowedItem = borrowedList[i].borrowedItem + 1
                    sameItemBorrowed = true
                    break
                }

            }
            if (!sameItemBorrowed) {
                val borrowedToolsDetails = BorrowedToolsDetails(
                    0,
                    toolsList[selectedToolsPosition].name,
                    1,
                    toolsList[selectedToolsPosition].imageId
                )
                borrowedList.add(borrowedToolsDetails)
            }
        } else {
            val borrowedToolsDetails = BorrowedToolsDetails(
                0,
                toolsList[selectedToolsPosition].name,
                1,
                toolsList[selectedToolsPosition].imageId
            )
            borrowedList.add(borrowedToolsDetails)
        }
        if (friendsDetails != null) {
            friendsDetails.borrowedToolsList = borrowedList
        }
       // add selected friend details in db
        friendsDetails?.let { appDb?.friendDao()?.insertFriend(it) }

    }

    @SuppressLint("WrongConstant")
    private fun showDialog() {
        friendsList.clear()
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.dialog_choose_friend)
        val rvFriend = dialog!!.findViewById(R.id.rvChooseFriend) as RecyclerView
        val friends = activity?.resources?.getStringArray(R.array.friends)
        for (i in friends?.indices!!) {
            val friendsDetails = friends.get(i)
            friendsList.add(friendsDetails)
        }
        val chooseFriendAdapter = activity?.let { ChooseFriendAdapter(friendsList, it) }
        rvFriend.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvFriend.adapter = chooseFriendAdapter
        chooseFriendAdapter?.setOnChooseFriendListListener(this)
        dialog!!.show()

    }
}