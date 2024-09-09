package com.jobhunter.app.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.usecase.GetLikedVacanciesUseCase
import com.jobhunter.domain.usecase.LikeVacancyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(
    getLikedVacanciesUseCase: GetLikedVacanciesUseCase,
    private val likeVacancyUseCase: LikeVacancyUseCase
) : ViewModel() {

    private val _isDataLoading = MutableStateFlow(true)
    val isDataLoading: LiveData<Boolean> = _isDataLoading.asLiveData()

    private val _messagesLiveData = MutableLiveData<String>()
    val messagesLiveData: LiveData<String> = _messagesLiveData

    private val _vacanciesLiveData = getLikedVacanciesUseCase
        .getLikedVacancies()
        .onEach {
            if (_isDataLoading.value) {
                _isDataLoading.emit(false)
            }
        }

    val vacancyLiveData = _vacanciesLiveData.asLiveData()

    fun likeVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            likeVacancyUseCase.likeVacancy(vacancy).onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { _messagesLiveData.postValue(it) }
            }
        }
    }

    fun dislikeVacancy(vacancy: Vacancy) {
        viewModelScope.launch {
            val result = likeVacancyUseCase.dislikeVacancy(vacancy)
            result.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { _messagesLiveData.postValue(it) }
            }
        }
    }
}