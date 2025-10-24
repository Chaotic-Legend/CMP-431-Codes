package com.example.nycjobs.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nycjobs.NYCOpenJobsApplication
import com.example.nycjobs.data.AppRepository
import com.example.nycjobs.model.JobPost
import com.example.nycjobs.util.TAG
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface JobPostingsUIState {
    data class Success(val data: List<JobPost>) : JobPostingsUIState
    data object Error : JobPostingsUIState
    data object Loading : JobPostingsUIState
    data object Ready : JobPostingsUIState
}

class JobPostingsViewModel(private val repository: AppRepository) : ViewModel() {

    var jobPostingsUIState: JobPostingsUIState by mutableStateOf(JobPostingsUIState.Ready)
        private set

    init {
        getJobPostings()
    }

    fun getJobPostings() {
        viewModelScope.launch {
            jobPostingsUIState = JobPostingsUIState.Loading
            jobPostingsUIState = try {
                JobPostingsUIState.Success(repository.getJobPostings())
            } catch (e: IOException) {
                Log.e(TAG, e.message ?: "IOException occurred")
                JobPostingsUIState.Error
            } catch (e: HttpException) {
                Log.e(TAG, e.message ?: "HttpException occurred")
                JobPostingsUIState.Error
            }
        }
    }

    fun getScrollPosition(): Int {
        return repository.getScrollPosition()
    }

    fun setScrollingPosition(position: Int) {
        repository.setScrollPosition(position)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                Log.i(TAG, "view model factory: getting application repository")
                val application = checkNotNull(extras[APPLICATION_KEY]) as NYCOpenJobsApplication
                val nycJobsRepository = application.container.appRepository
                return JobPostingsViewModel(repository = nycJobsRepository) as T
            }
        }
    }
}