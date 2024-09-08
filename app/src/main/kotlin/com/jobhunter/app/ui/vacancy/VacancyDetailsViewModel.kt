package com.jobhunter.app.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobhunter.domain.model.Vacancy
import com.jobhunter.domain.usecase.GetVacancyUseCase
import com.jobhunter.domain.usecase.LikeVacancyUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class VacancyDetailsViewModel @AssistedInject constructor(
    @Assisted private val vacancyId: String,
    private val getVacancyUseCase: GetVacancyUseCase,
    private val likeVacancyUseCase: LikeVacancyUseCase
) : ViewModel() {

    private val _isDataLoading = MutableLiveData(false)
    val isDataLoading: LiveData<Boolean> = _isDataLoading

    private val _messagesLiveData = MutableLiveData<String>()
    val messagesLiveData: LiveData<String> = _messagesLiveData

    private val _vacancyLiveData = MutableLiveData<Vacancy>()
    val vacancyLiveData = _vacancyLiveData as LiveData<Vacancy>

    init {
        viewModelScope.launch {
            _isDataLoading.postValue(true)

            getVacancyUseCase
                .getVacancyById(vacancyId)
                .onSuccess(_vacancyLiveData::postValue)
                .onFailure { throwable ->
                    throwable.localizedMessage
                        ?.takeIf { it.isNotBlank() }
                        ?.let(_messagesLiveData::postValue)
                }

            _isDataLoading.postValue(false)
        }
    }


    fun likeVacancy() {
        viewModelScope.launch {
            likeVacancyUseCase.likeVacancyById(vacancyId).onFailure { throwable ->
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