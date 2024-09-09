package com.jobhunter.app.ui.relevant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.usecase.GetAllVacanciesUseCase
import com.jobhunter.domain.usecase.LikeVacancyUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RelevantVacanciesViewModel @Inject constructor(
    getAllVacanciesUseCase: GetAllVacanciesUseCase,
    private val likeVacancyUseCase: LikeVacancyUseCase
) : ViewModel() {

    private val _isDataLoading = MutableStateFlow(true)
    val isDataLoading = _isDataLoading.asLiveData()


    private val _vacancies = MutableStateFlow(emptyList<Vacancy>())
    val vacancies = _vacancies.asLiveData()

    private val _messageFlow = Channel<String>()
    val messages = _messageFlow.receiveAsFlow().asLiveData()


    init {
        getAllVacanciesUseCase.getAllVacancies()
            .onEach {
                _vacancies.emit(it)
                if (_isDataLoading.value) {
                    _isDataLoading.emit(false)
                }
            }
            .launchIn(viewModelScope)
    }

    fun likeVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            val result = likeVacancyUseCase.likeVacancy(vacancy)
            result.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { _messageFlow.send(it) }
            }
        }
    }

    fun dislikeVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            val result = likeVacancyUseCase.dislikeVacancy(vacancy)
            result.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { _messageFlow.send(it) }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            message.takeIf { it.isNotBlank() }
                ?.let { _messageFlow.send(it) }
        }
    }
}