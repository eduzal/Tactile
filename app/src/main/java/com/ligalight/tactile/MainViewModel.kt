package com.ligalight.tactile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _animationFlags = MutableLiveData<BooleanArray>()
    val animationFlags get() = _animationFlags

    fun setAnimationFlags(flags: BooleanArray) {
        _animationFlags.postValue(flags)
    }

}
