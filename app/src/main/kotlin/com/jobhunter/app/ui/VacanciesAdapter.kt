package com.jobhunter.app.ui

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.jobhunter.domain.model.Vacancy

internal class VacanciesAdapter(
    vacanciesListener: VacanciesListener
    ) : ListDelegationAdapter<List<Vacancy>>(
    vacanciesDelegate(
        onClickToVacancy = vacanciesListener::openDetails,
        onClickToRespondButton = vacanciesListener::respond,
        onLikeVacancy = vacanciesListener::likeVacancy,
        onDislikeVacancy = vacanciesListener::dislikeVacancy
    ),
) {
    var vacancies: List<Vacancy>
        get() = items ?: emptyList()
        set(newList) {
            val diffUtilResult = DiffUtil.calculateDiff(VacanciesDiffUtil(vacancies, newList))
            items = newList
            diffUtilResult.dispatchUpdatesTo(this)
        }
}