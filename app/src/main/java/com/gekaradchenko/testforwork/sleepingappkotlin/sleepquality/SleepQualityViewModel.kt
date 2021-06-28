package com.gekaradchenko.testforwork.sleepingappkotlin.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(private val sleepNightKey: Long = 0L, val database: SleepDatabaseDao) :
    ViewModel() {

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }


    fun onSetSleepQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val toNight = database.get(sleepNightKey) ?: return@withContext
                toNight.sleepQuality = quality
                database.update(toNight)
            }
            _navigateToSleepTracker.value = true
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}