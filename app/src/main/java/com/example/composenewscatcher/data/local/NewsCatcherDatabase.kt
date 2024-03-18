package com.example.composenewscatcher.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composenewscatcher.data.model.Article

@Database (
    entities = [Article::class],
    version = 2
)
abstract class NewsCatcherDatabase: RoomDatabase() {

    abstract fun getNewsCatcherDao(): NewsCatcherDao

}