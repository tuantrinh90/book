package com.dz.models.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "Book",foreignKeys = [ForeignKey(entity = Category::class,parentColumns = ["id"],childColumns = ["category_id"])])
@Entity(tableName = "Book")
data class Book(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "author")
        var author: String,
        @ColumnInfo(name = "image")
        var image: String,
        @ColumnInfo(name = "favorite")
        var favorite: Boolean,
        @ColumnInfo(name = "category_id")
        var category_id: Int,
        @ColumnInfo(name = "type")
        var type: String,
        @ColumnInfo(name = "publishing")
        var publishing: String
)