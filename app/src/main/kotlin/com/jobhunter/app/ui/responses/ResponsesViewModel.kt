package com.jobhunter.app.ui.responses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResponsesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Экран с откликами на вакансии"
    }
    val text: LiveData<String> = _text
}