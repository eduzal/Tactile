package com.example.tactile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _animationValues = MutableLiveData<LongArray>()
    val animationValues: MutableLiveData<LongArray> get() = _animationValues

    fun setAnimationValues(values: LongArray) {
        _animationValues.postValue(values)
    }
}
