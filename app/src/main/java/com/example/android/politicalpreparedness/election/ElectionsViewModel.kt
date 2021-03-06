package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.data.repository.ElectionRepository
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.data.domain.ElectionDomain

class ElectionsViewModel(
    app: Application,
    private val repository: ElectionRepository,
) : BaseViewModel(app) {

    private val _elections = repository.elections
    val elections: LiveData<List<ElectionDomain>>
        get() = _elections
    private val _savedElections = repository.savedElections
    val savedElections: LiveData<List<ElectionDomain>>
        get() = _savedElections

    fun forceUpdateElectionsData() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                repository.forceUpdateElectionsData()
                showLoading.value = false
            } catch (e: Exception) {
                showErrorMessage.postValue(e.localizedMessage)
                showLoading.value = false
            }
        }
    }

    fun updateElectionsData() {
        showLoading.value = true
        viewModelScope.launch {
            try {
                repository.updateElectionsData()
                showLoading.value = false
            } catch (e: Exception) {
                showErrorMessage.postValue(e.localizedMessage)
                showLoading.value = false
            }
        }
    }

    fun navigateToVoterInfo(election: ElectionDomain) {
        navigationCommand.postValue(
            NavigationCommand.To(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    election.id
                )
            )
        )
    }

}
