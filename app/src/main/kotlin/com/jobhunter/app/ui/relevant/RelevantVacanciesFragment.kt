package com.jobhunter.app.ui.relevant

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jobhunter.app.R
import com.jobhunter.app.app.App
import com.jobhunter.app.databinding.FragmentRelevantVacanciesBinding
import com.jobhunter.app.ui.VacanciesAdapter
import com.jobhunter.app.ui.VacanciesListener
import com.jobhunter.app.utils.applyWindowInsets
import com.jobhunter.app.utils.createSpacerDrawable
import com.jobhunter.app.utils.hide
import com.jobhunter.app.utils.show
import com.jobhunter.domain.model.Vacancy

class RelevantVacanciesFragment : Fragment() {

    private var _binding: FragmentRelevantVacanciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RelevantVacanciesViewModel by viewModels {
        viewModelFactory {
            initializer {
                (requireActivity().application as App).appComponent.getRelevantVacanciesViewModel()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRelevantVacanciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contentLayout.applyWindowInsets(WindowInsetsCompat.Type.statusBars())
        setupRecyclerView()

        viewModel.isDataLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.relevantVacanciesRecyclerView.hide()
                binding.headerLayout.hide()
                binding.vacanciesLoadingProgressBar.show()
            } else {
                binding.vacanciesLoadingProgressBar.hide()
                binding.relevantVacanciesRecyclerView.show()
                binding.headerLayout.show()
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.WHITE)
                .setTextColor(Color.BLACK)
                .setActionTextColor(Color.RED)
                .apply {
                    setAction("Close") { dismiss() }
                }
                .show()
        }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun setupRecyclerView() {
        val vacanciesAdapter = VacanciesAdapter(
            vacanciesListener = object : VacanciesListener {
                override fun openDetails(vacancy: Vacancy) {
                    val action = RelevantVacanciesFragmentDirections.showVacancyDetailsAction(vacancy.id)
                    findNavController().navigate(action)
                }

                override fun respond(vacancy: Vacancy) {
                    viewModel.sendMessage(
                        "Поздравляем, вы приняты на работу в компанию ${vacancy.company} на должность ${vacancy.title}!"
                    )
                }

                override fun likeVacancy(vacancy: Vacancy) {
                    viewModel.likeVacancy(vacancy)
                }
            }
        )

        binding.relevantVacanciesRecyclerView.adapter = vacanciesAdapter

        val spaceDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL).apply {
            setDrawable(createSpacerDrawable(8))
        }
        binding.relevantVacanciesRecyclerView.addItemDecoration(spaceDecoration)

        viewModel.vacancies.observe(viewLifecycleOwner) { vacancyList ->
            vacanciesAdapter.vacancies = vacancyList

            binding.vacanciesCount.text = resources.getQuantityString(
                R.plurals.vacancies_count,
                vacancyList.size,
                vacancyList.size
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}