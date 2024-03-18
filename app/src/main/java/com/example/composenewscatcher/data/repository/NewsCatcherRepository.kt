package com.example.composenewscatcher.data.repository

import com.example.composenewscatcher.data.local.NewsCatcherLocalDataSource
import com.example.composenewscatcher.data.remote.newscatcherapi.NewsCatcherApiRemoteDataSource
import javax.inject.Inject

class NewsCatcherRepository @Inject constructor(
    private val _newsCatcherApiRemoteDataSource: NewsCatcherApiRemoteDataSource,
    private val _newsCatcherLocalDataSource: NewsCatcherLocalDataSource
){
    inner class RemoteDataSource {
        val newsCatcher = _newsCatcherApiRemoteDataSource
    }

    inner class LocalDataSource {
        val newsCatcher = _newsCatcherLocalDataSource
    }
}