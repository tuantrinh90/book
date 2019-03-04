package com.dz.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dz.interactors.databases.IDbDao
import com.dz.models.database.Book
import com.dz.models.database.Category
import com.dz.models.database.LinkBook
import com.dz.models.database.PageBook
import com.squareup.haha.perflib.Instance

@Database(entities = [Book::class, LinkBook::class, PageBook::class, Category::class], version = 1, exportSchema = false)
abstract class AppData : RoomDatabase() {
    abstract fun idbDao(): IDbDao

    companion object {
        var INSTANCE: AppData? = null
        fun getAppData(context: Context): AppData? {
            if (INSTANCE == null) {
                synchronized(AppData::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppData::class.java, "my-db").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase() {
            INSTANCE = null
        }
    }

}