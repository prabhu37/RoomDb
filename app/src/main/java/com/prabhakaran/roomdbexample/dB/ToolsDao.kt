package com.prabhakaran.roomdbexample.dB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prabhakaran.roomdbexample.model.ToolsDetails

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
@Dao
interface ToolsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(toolsList: List<ToolsDetails>)

    @Query("SELECT * FROM tools")
    fun getAll(): List<ToolsDetails>

    @Query("SELECT * FROM tools WHERE name = :name")
    fun getToolsDetails(name: String): ToolsDetails


    @Query("UPDATE tools SET borrowedItem=:count WHERE name = :name")
    fun update(count: Int, name: String)

    @Query("UPDATE tools SET borrowedItem=:count WHERE name = :name")
    fun updateBorrowedCount(count: Int, name: String)


    @Query("SELECT COUNT(*) FROM tools")
    fun getCount(): Int
}