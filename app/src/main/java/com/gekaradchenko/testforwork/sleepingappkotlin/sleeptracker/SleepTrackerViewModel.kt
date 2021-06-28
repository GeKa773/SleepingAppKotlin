package com.gekaradchenko.testforwork.sleepingappkotlin.sleeptracker

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.gekaradchenko.testforwork.sleepingappkotlin.Unit
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepDatabaseDao
import com.gekaradchenko.testforwork.sleepingappkotlin.database.SleepNight
import kotlinx.coroutines.*

class SleepTrackerViewMode(val database: SleepDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScore = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var toNight = MutableLiveData<SleepNight?>()
    private val night = database.getAllNights()


    val startButtonVisible = Transformations.map(toNight) {
        null == it
    }
    val stopButtonVisible = Transformations.map(toNight) {
        null != it
    }

    val clearButtonVisible = Transformations.map(night) {
        it?.isNotEmpty()
    }

    val nightsString = Transformations.map(night) {
        Unit.formatNights(it, application.resources)
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }


    init {
        initializeTonight()

    }

    private fun initializeTonight() {
        uiScore.launch {
            toNight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {

        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScore.launch {
            val newNight = SleepNight()
            insert(newNight)

            toNight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    fun onStopTracking() {
        uiScore.launch {
            val oldNight = toNight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }

    }

    private suspend fun update(oldNight: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(oldNight)
        }


    }

    fun onClear() {
        uiScore.launch {
            clear()
            toNight.value = null
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }

    }


}