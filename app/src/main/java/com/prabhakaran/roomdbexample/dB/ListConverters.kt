package com.prabhakaran.roomdbexample.dB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.prabhakaran.roomdbexample.model.BorrowedToolsDetails

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/
class ListConverters {
        @TypeConverter
        fun listToJson(value: List<BorrowedToolsDetails>?): String {
            return Gson().toJson(value)
        }

        @TypeConverter
        fun jsonToList(value: String): List<BorrowedToolsDetails>? {
            val objects = Gson().fromJson(value, Array<BorrowedToolsDetails>::class.java) as Array<BorrowedToolsDetails>
            return objects.toList()
        }



}