package com.prabhakaran.roomdbexample.dB

import androidx.room.*
import com.prabhakaran.roomdbexample.model.FriendsDetails

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
@Dao
interface FriendsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(toolsList: List<FriendsDetails>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(friendsDetails: FriendsDetails)


    @Query("SELECT * FROM friends")
    fun getAll(): List<FriendsDetails>


    @Query("SELECT * FROM friends WHERE friendName = :name")
    fun getFriendDetails(name: String): FriendsDetails

    @Delete
    fun deleteFriendDetails(friendsDetails: FriendsDetails)
    @Query("SELECT COUNT(*) FROM friends")
    fun getCount(): Int


}