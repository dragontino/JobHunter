package com.jobhunter.app.ui

import com.jobhunter.domain.model.Vacancy

internal interface VacanciesListener {
    fun openDetails(vacancy: Vacancy)
    fun respond(vacancy: Vacancy)
    fun likeVacancy(vacancy: Vacancy)
    fun dislikeVacancy(vacancy: Vacancy)
}