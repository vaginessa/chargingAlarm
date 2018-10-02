package com.chargingwatts.chargingalarm.ui.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.chargingwatts.chargingalarm.repository.UserProfileRepository
import com.chargingwatts.chargingalarm.util.AbsentLiveData
import com.chargingwatts.chargingalarm.vo.Resource
import com.chargingwatts.chargingalarm.vo.UserDetail
import javax.inject.Inject

class FEProfileViewModel
@Inject constructor(userProfileRepository: UserProfileRepository) : ViewModel() {
    private val _userId = MutableLiveData<String>()
    val employeeId: LiveData<String>
        get() = _userId

    val feProfileData: LiveData<Resource<UserDetail>> = Transformations.switchMap(_userId) { userId ->
        if (userId == null) {
            AbsentLiveData.create()
        } else {
            userProfileRepository.loadUserDetail(userId)
        }
    }

    fun setEmployeeId(userId: String) {
        if (_userId.value !== userId) {
            _userId.value = userId
        }
    }

    fun retry() {
        _userId.value?.let {
            _userId.value = it
        }
    }
}