package com.example.composenewscatcher.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.composenewscatcher.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsCatcherDao {

    @Upsert
    suspend fun upsert(article: Article)

    @Upsert
    suspend fun upsertAll(vararg article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Delete
    suspend fun deleteAll(vararg article: Article)

    @Query ("SELECT * FROM articles_table WHERE isCached = 1")
    fun getAllCachedArticlesFromDb(): Flow<List<Article>>

    @Query ("SELECT * FROM articles_table WHERE isSaved = 1")
    fun getAllSavedArticlesFromDb(): Flow<List<Article>>

}