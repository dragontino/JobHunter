package com.jobhunter.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.usecase.GetAllVacanciesUseCase
import com.jobhunter.domain.usecase.GetOffersUseCase
import com.jobhunter.domain.usecase.LikeVacancyUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


class SearchViewModel @Inject constructor(
    getOffersUseCase: GetOffersUseCase,
    getAllVacanciesUseCase: GetAllVacanciesUseCase,
    private val likeVacancyUseCase: LikeVacancyUseCase
) : ViewModel() {
    private val _isOffersLoading = MutableStateFlow(true)
    val isOffersLoading = _isOffersLoading.asLiveData()

    private val _isVacanciesLoading = MutableStateFlow(true)
    val isVacanciesLoading = _isVacanciesLoading.asLiveData()

    private val _offers = getOffersUseCase.getOffers().onEach {
        if (_isOffersLoading.value) {
            _isOffersLoading.emit(false)
        }
    }
    val offers = _offers.asLiveData()

    private val _vacancies: Flow<List<Vacancy>> = getAllVacanciesUseCase
        .getAllVacancies()
        .onEach {
            if (_isVacanciesLoading.value) {
                _isVacanciesLoading.emit(false)
            }
        }
    val vacancies = _vacancies
        .map { it.slice(0 ..< 3) }
        .asLiveData()

    private val _messageLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> = _messageLiveData

    val vacanciesCount = _vacancies.map { it.size }.asLiveData()

    fun likeVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            val result = likeVacancyUseCase.likeVacancy(vacancy)
            result.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf(String::isNotBlank)
                    ?.let(_messageLiveData::postValue)
            }
        }
    }

    fun dislikeVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            val result = likeVacancyUseCase.dislikeVacancy(vacancy)
            result.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { _messageLiveData.postValue(it) }
            }
        }
    }

    fun sendMessage(message: String) {
        message.takeIf(String::isNotBlank)?.let(_messageLiveData::postValue)
    }
}