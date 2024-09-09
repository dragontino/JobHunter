package com.jobhunter.app.ui.favourites

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
import com.jobhunter.app.databinding.FragmentFavouritesBinding
import com.jobhunter.app.ui.VacanciesAdapter
import com.jobhunter.app.ui.VacanciesListener
import com.jobhunter.app.utils.applyWindowInsets
import com.jobhunter.app.utils.createSpacerDrawable
import com.jobhunter.app.utils.hide
import com.jobhunter.app.utils.show
import com.jobhunter.domain.model.Vacancy

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModels<FavouritesViewModel> {
        viewModelFactory {
            initializer {
                (requireActivity().application as App).appComponent.getFavouritesViewModel()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favouritesToolbar.applyWindowInsets(WindowInsetsCompat.Type.statusBars())

        viewModel.isDataLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.dataLoadingProgressBar.show()
                binding.nestedScrollView.hide()
            } else {
                binding.nestedScrollView.show()
                binding.dataLoadingProgressBar.hide()
            }
        }

        viewModel.messagesLiveData.observe(viewLifecycleOwner) {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                .setTextColor(Color.BLACK)
                .setBackgroundTint(Color.WHITE)
                .show()
        }

        val vacanciesAdapter = VacanciesAdapter(
            object : VacanciesListener {
                override fun openDetails(vacancy: Vacancy) {
                    val action = FavouritesFragmentDirections
                        .showVacancyDetailsAction(vacancyId = vacancy.id)
                    findNavController().navigate(action)
                }

                override fun respond(vacancy: Vacancy) {}

                override fun likeVacancy(vacancy: Vacancy) {
                    viewModel.likeVacancy(vacancy)
                }

                override fun dislikeVacancy(vacancy: Vacancy) {
                    viewModel.dislikeVacancy(vacancy)
                }
            }
        )

        binding.vacanciesRecyclerView.adapter = vacanciesAdapter

        val itemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL).apply {
            setDrawable(createSpacerDrawable(spaceDp = 16))
        }
        binding.vacanciesRecyclerView.addItemDecoration(itemDecoration)

        viewModel.vacancyLiveData.observe(viewLifecycleOwner) { vacancies ->
            vacanciesAdapter.vacancies = vacancies
            binding.vacanciesCount.text = resources.getQuantityString(
                R.plurals.vacancies_count,
                vacancies.size,
                vacancies.size
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}