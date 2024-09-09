package com.jobhunter.app.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class EntryCodeViewModel : ViewModel() {
    private val _code = MutableStateFlow<List<String?>>(List(4) { null })
    val code = _code.asLiveData()

    fun updateCodeNumber(number: String, position: Int) {
        _code.update { array ->
            array.toMutableList().apply { this[position] = number }
        }
    }
}