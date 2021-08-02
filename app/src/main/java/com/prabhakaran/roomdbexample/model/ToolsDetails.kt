package com.prabhakaran.roomdbexample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by P.Prabhakaran on 09,November,2019
 **/
@Entity(tableName = "tools")
 data class ToolsDetails(var toolsId:Int) {
    @PrimaryKey
    var id:Int = 0
    var name:String = ""
    var totalItem:Int = 0
    var borrowedItem:Int = 0
    var imageId:Int = 0
    constructor(
        id:Int,
        name:String,
        totalItem:Int,
        borrowedItem:Int,
        imageId:Int) : this(toolsId = id)
    {
        this.id = id
        this.name = name
        this.totalItem = totalItem
        this.borrowedItem = borrowedItem
        this.imageId = imageId
    }
}
