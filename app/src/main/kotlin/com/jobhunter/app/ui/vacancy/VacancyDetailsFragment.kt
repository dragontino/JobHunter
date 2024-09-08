package com.jobhunter.app.ui.vacancy

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.jobhunter.app.R
import com.jobhunter.app.app.App
import com.jobhunter.app.databinding.FragmentVacancyDetailsBinding
import com.jobhunter.app.ui.questionsDelegate
import com.jobhunter.app.ui.respond.RespondBottomSheetFragment
import com.jobhunter.app.ui.respond.RespondDialogFragment
import com.jobhunter.app.utils.applyWindowInsets
import com.jobhunter.app.utils.createSpacerDrawable
import com.jobhunter.app.utils.hide
import com.jobhunter.app.utils.show
import com.jobhunter.app.utils.toTitleCase

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: VacancyDetailsFragmentArgs by navArgs()

    private val viewModel: VacancyDetailsViewModel by viewModels {
        viewModelFactory {
            initializer {
                (requireActivity().application as App)
                    .appComponent
                    .getVacancyDetailsViewModel()
                    .create(args.vacancyId)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appliedNumber.iconView.setImageResource(R.drawable.icon_applied_number)
        binding.lookingNumber.iconView.setImageResource(R.drawable.icon_looking)

        binding.vacancyToolbar.applyWindowInsets(WindowInsetsCompat.Type.statusBars())
        binding.vacancyToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.isDataLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.dataLoadingProgressBar.show()
                binding.nestedScrollView.hide()
                binding.vacancyToolbar.hide()
            } else {
                binding.nestedScrollView.show()
                binding.vacancyToolbar.show()
                binding.dataLoadingProgressBar.hide()
            }
        }

        viewModel.messagesLiveData.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                .setTextColor(Color.BLACK)
                .setBackgroundTint(Color.WHITE)
                .show()
        }

        binding.vacancyToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_like -> {
                    viewModel.likeVacancy()
                    true
                }

                else -> false
            }
        }

        setupLayout()
    }


    private fun setupLayout() {
        val questionsAdapter = ListDelegationAdapter(
            questionsDelegate {
                val dialog = RespondDialogFragment.newInstance(it)
                dialog.show(
                    requireActivity().supportFragmentManager,
                    "RespondDialogFragment"
                )
            }
        )

        binding.questionsRecyclerView.adapter = questionsAdapter
        val dividerDeclaration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            setDrawable(createSpacerDrawable(8))
        }
        binding.questionsRecyclerView.addItemDecoration(dividerDeclaration)

        viewModel.vacancyLiveData.observe(viewLifecycleOwner) { vacancy ->
            with(binding) {
                vacancyToolbar.menu.findItem(R.id.menu_item_like).apply {
                    isEnabled = true
                    when {
                        vacancy.isFavorite -> setIcon(R.drawable.icon_favourite_active)
                        else -> setIcon(R.drawable.icon_favourite)
                    }
                }
                vacancyTitle.text = vacancy.title
                salary.text = vacancy.salary.full
                experience.text = getString(R.string.experience, vacancy.experience.text)
                schedules.text = vacancy.schedules.joinToString().toTitleCase()
                appliedNumber.apply {
                    when (val appliedNumber = vacancy.appliedNumber) {
                        null -> root.hide()
                        else -> {
                            root.show()
                            textView.text = resources.getQuantityString(
                                R.plurals.more_vacancies,
                                appliedNumber,
                                appliedNumber
                            )
                        }
                    }
                }
                lookingNumber.apply {
                    when (val lookingNumber = vacancy.lookingNumber) {
                        null -> root.hide()
                        else -> {
                            root.show()
                            textView.text = resources.getQuantityString(
                                R.plurals.looking_number_2,
                                lookingNumber,
                                lookingNumber
                            )
                        }
                    }
                }
                companyTitle.companyTextView.text = vacancy.company
                companyAddress.text = vacancy.address.toString()
                companyDescription.apply {
                    when (val description = vacancy.description) {
                        null -> hide()
                        else -> {
                            show()
                            text = description
                        }
                    }
                }
                responsibilities.text = vacancy.responsibilities
                questionsAdapter.items = vacancy.questions
                respondButton.setOnClickListener {
                    RespondBottomSheetFragment().show(
                        requireActivity().supportFragmentManager,
                        "RespondBottomSheetFragment"
                    )
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}