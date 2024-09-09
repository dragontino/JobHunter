package com.jobhunter.app.ui

import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.DrawableRes
import androidx.core.widget.doAfterTextChanged
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.jobhunter.app.R
import com.jobhunter.app.databinding.CodeNumberLayoutBinding
import com.jobhunter.app.databinding.OfferBinding
import com.jobhunter.app.databinding.VacancyBinding
import com.jobhunter.app.databinding.VacancyQuestionBinding
import com.jobhunter.app.utils.hide
import com.jobhunter.app.utils.show
import com.jobhunter.domain.model.Offer
import com.jobhunter.domain.model.OfferId
import com.jobhunter.domain.model.Vacancy
import java.time.format.DateTimeFormatter

fun vacanciesDelegate(
    onClickToVacancy: (Vacancy) -> Unit,
    onClickToRespondButton: (Vacancy) -> Unit,
    onLikeVacancy: (Vacancy) -> Unit,
    onDislikeVacancy: (Vacancy) -> Unit
) =
    adapterDelegateViewBinding<Vacancy, Vacancy, VacancyBinding>(
        viewBinding = { layoutInflater, parent ->
            VacancyBinding.inflate(layoutInflater, parent, false)
        }
    ) {

        bind {
            binding.root.setOnClickListener {
                onClickToVacancy(item)
            }
            binding.respondButton.setOnClickListener {
                onClickToRespondButton(item)
            }
            binding.isFavourite.setOnClickListener {
                if (item.isFavorite) onDislikeVacancy(item) else onLikeVacancy(item)
            }

            with(binding.isFavourite) {
                when {
                    item.isFavorite -> setBackgroundResource(R.drawable.icon_favourite_active)
                    else -> setBackgroundResource(R.drawable.icon_favourite)
                }
            }

            binding.lookingNumber.apply {
                when (val lookingNumber = item.lookingNumber) {
                    null -> hide()
                    else -> text = context.resources.getQuantityString(
                        R.plurals.looking_number_1, lookingNumber, lookingNumber
                    )
                }
            }

            binding.title.text = item.title
            binding.city.text = item.address.town
            binding.companyTitle.companyTextView.text = item.company
            binding.experienceTextView.text = item.experience.previewText

            binding.publishedDate.apply {
                val formatter = DateTimeFormatter.ofPattern("d MMMM")
                text = getString(
                    R.string.published_date,
                    item.publishedDate.format(formatter)
                )
            }
        }
    }



fun offersDelegate(itemClickListener: (Offer) -> Unit) =
    adapterDelegateViewBinding<Offer, Offer, OfferBinding>(
        viewBinding = { inflater, parent -> OfferBinding.inflate(inflater, parent, false) }
    ) {
        @DrawableRes
        fun getOfferIconById(id: OfferId): Int = when (id) {
            OfferId.NearVacancies -> R.drawable.icon_near_vacancies
            OfferId.LevelUpResume -> R.drawable.icon_level_up_resume
            OfferId.TemporaryJob -> R.drawable.icon_temporary_job
        }

        bind {
            binding.root.setOnClickListener {
                itemClickListener(item)
            }

            when (val offerId = item.id) {
                null -> binding.iconView.hide(strongly = false)
                else -> {
                    binding.iconView.setImageResource(getOfferIconById(offerId))
                    binding.iconView.show()
                }
            }

            binding.title.text = item.title
            binding.title.maxLines = if (item.button == null) 3 else 2

            when (val button = item.button) {
                null -> binding.action.hide()
                else -> {
                    binding.action.text = button.text
                    binding.action.show()
                }
            }
        }
    }


fun questionsDelegate(onClick: (String) -> Unit) =
    adapterDelegateViewBinding<String, String, VacancyQuestionBinding>(
        viewBinding = { layoutInflater, parent ->
            VacancyQuestionBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        bind {
            binding.root.setOnClickListener {
                onClick(item)
            }
            binding.root.text = item
        }
    }


fun codeNumbersDelegate(onUpdateText: (String, Int) -> Unit) =
    adapterDelegateViewBinding<String, String, CodeNumberLayoutBinding>(
        viewBinding = { layoutInflater, parent ->
            CodeNumberLayoutBinding.inflate(layoutInflater, parent, false)
        }
    ) {
        binding.root.imeOptions = EditorInfo.IME_ACTION_NEXT

        bind {
            binding.root.setText(item)

            if (bindingAdapterPosition == 0) {
                binding.root.requestFocus()
            }

            binding.root.doAfterTextChanged { text ->
                onUpdateText(text?.toString() ?: "", bindingAdapterPosition)
                if (!text.isNullOrBlank()) {
                    when (val nextView = binding.root.focusSearch(View.FOCUS_RIGHT)) {
                        null -> binding.root.clearFocus()
                        else -> nextView.requestFocus()
                    }
                    binding.root.focusable = View.NOT_FOCUSABLE
                    binding.root.keyListener = null
                }
            }
        }
    }