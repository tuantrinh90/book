package com.dz.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

//@Entity(tableName = "PageBook", foreignKeys = [ForeignKey(entity = Book::class, parentColumns = ["id"], childColumns = ["book_id"])])
@Entity(tableName = "PageBook")
data class PageBook(
        @NotNull
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val pageBookID: Int,
        @ColumnInfo(name = "description")
        val description: String,
        @ColumnInfo(name = "number_page")
        val numberPage: Int,
        @ColumnInfo(name = "book_id")
        val book_id: Int
)