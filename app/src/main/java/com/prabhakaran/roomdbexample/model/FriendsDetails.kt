package com.prabhakaran.roomdbexample.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.prabhakaran.roomdbexample.dB.ListConverters

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
@Entity(tableName= "friends")
data class FriendsDetails(var id: Int) {
  @PrimaryKey
    var friendId: Int = 0
    var friendName: String = ""
    @TypeConverters(ListConverters::class)
   var borrowedToolsList: List<BorrowedToolsDetails>? = null

    constructor(
        friendId: Int,
        friendName: String,
        borrowedToolsList: List<BorrowedToolsDetails>
    ) : this(id = friendId) {
        this.friendId = friendId
        this.friendName = friendName
        this.borrowedToolsList = borrowedToolsList
    }
}