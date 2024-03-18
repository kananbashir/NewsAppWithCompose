package com.example.composenewscatcher.di

import android.content.Context
import androidx.room.Room
import com.example.composenewscatcher.data.local.NewsCatcherDao
import com.example.composenewscatcher.data.local.NewsCatcherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object LocalDatabaseModule {

    @Provides
    @Singleton
    fun provideNewsCatcherDatabase(@ApplicationContext context: Context): NewsCatcherDatabase {
        return Room.databaseBuilder(
            context,
            NewsCatcherDatabase::class.java,
            "news_catcher_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsCatcherDao(newsCatcherDatabase: NewsCatcherDatabase): NewsCatcherDao {
        return newsCatcherDatabase.getNewsCatcherDao()
    }

}