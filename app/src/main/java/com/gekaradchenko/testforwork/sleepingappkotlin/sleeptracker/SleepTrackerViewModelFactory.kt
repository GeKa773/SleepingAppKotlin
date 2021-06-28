package com.gekaradchenko.testforwork.sleepingappkotlin.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepDatabaseDao
import java.lang.IllegalArgumentException

class SleepTrackerViewModelFactory(
    private val dataSource: SleepDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewMode::class.java)) {
            return SleepTrackerViewMode(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}