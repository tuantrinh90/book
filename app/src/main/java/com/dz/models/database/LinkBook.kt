package com.dz.models.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//@Entity(tableName = "LinkBook",foreignKeys = [ForeignKey(entity = Book::class,parentColumns = ["id"],childColumns = ["book_id"])])
@Entity(tableName = "LinkBook")
data class LinkBook(
        @NonNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val pageBookID: Int,
        @ColumnInfo(name = "description")
        val description: String,
        @ColumnInfo(name = "book_id")
        val bookId : Int
)