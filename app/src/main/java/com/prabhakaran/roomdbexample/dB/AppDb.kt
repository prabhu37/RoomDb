package com.prabhakaran.roomdbexample.dB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prabhakaran.roomdbexample.model.FriendsDetails
import com.prabhakaran.roomdbexample.model.ToolsDetails

/**
 * Created by P.Prabhakaran on 10,November,2019
 **/

@Database(entities = [ToolsDetails::class,FriendsDetails::class], version = 2, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun friendDao(): FriendsDao
    abstract fun toolsDao(): ToolsDao
    companion object{
        private const val DB_NAME = "AppDB.db"
        @Volatile
        private var instance: AppDb? = null

        fun getAppDB(context: Context): AppDb? {
            if (instance == null) {
                synchronized(AppDb::class) {
                    if(instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDb::class.java,
                            DB_NAME
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return instance
        }
    }
    }
