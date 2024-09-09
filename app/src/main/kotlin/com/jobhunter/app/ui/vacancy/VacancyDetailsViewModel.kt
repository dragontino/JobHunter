package com.jobhunter.app.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jobhunter.domain.usecase.GetVacancyUseCase
import com.jobhunter.domain.usecase.LikeVacancyUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class VacancyDetailsViewModel @AssistedInject constructor(
    @Assisted private val vacancyId: String,
    getVacancyUseCase: GetVacancyUseCase,
    private val likeVacancyUseCase: LikeVacancyUseCase
) : ViewModel() {

    private val _isDataLoading = MutableLiveData(true)
    val isDataLoading: LiveData<Boolean> = _isDataLoading

    private val _messagesLiveData = MutableLiveData<String>()
    val messagesLiveData: LiveData<String> = _messagesLiveData

    private val _vacancyFlow = getVacancyUseCase
        .getVacancyById(vacancyId)
        .onEach { _isDataLoading.postValue(false) }
        .catch { throwable ->
            throwable.localizedMessage
                ?.takeIf { it.isNotBlank() }
                ?.let { _messagesLiveData.postValue(it) }
        }

    val vacancyLiveData = _vacancyFlow.asLiveData()


    fun likeVacancy() {
        viewModelScope.launch {
            val vacancy = vacancyLiveData.value ?: return@launch
            val result = when {
                vacancy.isFavorite -> likeVacancyUseCase.dislikeVacancy(vacancy)
                else -> likeVacancyUseCase.likeVacancy(vacancy)
            }
            result.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { _messagesLiveData.postValue(it) }
            }
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(@Assisted vacancyId: String): VacancyDetailsViewModel
    }
}