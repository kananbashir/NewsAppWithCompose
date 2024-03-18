package com.example.composenewscatcher.data.local

import com.example.composenewscatcher.data.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsCatcherLocalDataSource @Inject constructor(private val newsCatcherDao: NewsCatcherDao) {

    suspend fun upsert(article: Article) {
        newsCatcherDao.upsert(article)
    }

    suspend fun upsertAll(vararg article: Article) {
        newsCatcherDao.upsertAll(*article)
    }

    suspend fun delete(article: Article) {
        newsCatcherDao.delete(article)
    }

    suspend fun deleteAll(vararg article: Article) {
        newsCatcherDao.deleteAll(*article)
    }

    fun getAllCachedArticles(): Flow<List<Article>> = newsCatcherDao.getAllCachedArticlesFromDb()

    fun getAllSavedArticles(): Flow<List<Article>> = newsCatcherDao.getAllSavedArticlesFromDb()

}