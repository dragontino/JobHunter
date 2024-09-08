package com.jobhunter.app.di

import com.jobhunter.app.ui.favourites.FavouritesViewModel
import com.jobhunter.app.ui.relevant.RelevantVacanciesViewModel
import com.jobhunter.app.ui.search.SearchViewModel
import com.jobhunter.app.ui.vacancy.VacancyDetailsViewModel
import dagger.Component

@Component(modules = [DomainModule::class, DataModule::class])
interface AppComponent {
    fun getSearchViewModel(): SearchViewModel

    fun getRelevantVacanciesViewModel(): RelevantVacanciesViewModel

    fun getVacancyDetailsViewModel(): VacancyDetailsViewModel.Factory

    fun getFavouritesViewModel(): FavouritesViewModel
}