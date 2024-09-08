package com.jobhunter.app.ui.search

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
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
import com.jobhunter.app.databinding.FragmentSearchBinding
import com.jobhunter.app.ui.VacanciesAdapter
import com.jobhunter.app.ui.VacanciesListener
import com.jobhunter.app.utils.applyWindowInsets
import com.jobhunter.app.utils.createSpacerDrawable
import com.jobhunter.app.utils.hide
import com.jobhunter.app.utils.show
import com.jobhunter.domain.model.Vacancy

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactory {
            initializer {
                (requireActivity().application as App).appComponent.getSearchViewModel()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contentLayout.applyWindowInsets(WindowInsetsCompat.Type.statusBars())
        setupOffersRecyclerView()
        setupVacanciesRecyclerView()

        viewModel.messageLiveData.observe(viewLifecycleOwner) { message ->
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setTextColor(Color.BLACK)
                .setBackgroundTint(Color.WHITE)
                .setActionTextColor(Color.RED)
                .apply {
                    setAction("Close") {
                        dismiss()
                    }
                }
                .show()
        }


        binding.moreVacancies.setOnClickListener {
            val action = SearchFragmentDirections.showRelevantVacanciesAction()
            findNavController().navigate(action)
        }

        viewModel.vacanciesCount.observe(viewLifecycleOwner) {
            binding.moreVacancies.text = resources.getQuantityString(
                R.plurals.more_vacancies, it, it
            )
        }


        viewModel.isOffersLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.offersLoadingProgressBar.show()
                binding.offersRecyclerView.hide()
                binding.offersLoadingProgressBar.progressTintList = ColorStateList.valueOf(Color.WHITE)
            } else {
                binding.offersLoadingProgressBar.hide()
                binding.offersRecyclerView.show()
            }
        }

        viewModel.isVacanciesLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.vacanciesLoadingProgressBar.show()
                binding.vacanciesRecyclerView.hide()
                binding.moreVacancies.hide()
            } else {
                binding.vacanciesRecyclerView.show()
                binding.vacanciesLoadingProgressBar.hide()
                binding.moreVacancies.show()
            }
        }
    }


    private fun setupOffersRecyclerView() {
        val offersAdapter = OffersAdapter(
            onItemClickListener = { offer -> sendBrowserIntent(offer.link) }
        )
        binding.offersRecyclerView.adapter = offersAdapter
        val itemDecoration = DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL).apply {
            setDrawable(createSpacerDrawable(8))
        }
        binding.offersRecyclerView.addItemDecoration(itemDecoration)

        viewModel.offers.observe(viewLifecycleOwner) { offers ->
            if (offers.isEmpty()) {
                binding.offersRecyclerView.hide()
                binding.moreVacancies.hide()
            } else {
                offersAdapter.offers = offers
                binding.offersRecyclerView.show()
                binding.moreVacancies.show()
            }
        }
    }


    private fun setupVacanciesRecyclerView() {
        val vacanciesAdapter = VacanciesAdapter(
            vacanciesListener = object : VacanciesListener {
                override fun openDetails(vacancy: Vacancy) {
                    val action = SearchFragmentDirections.showVacancyDetailsAction(vacancy.id)
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
            },
        )
        binding.vacanciesRecyclerView.adapter = vacanciesAdapter
        val itemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL).apply {
            setDrawable(createSpacerDrawable(16))
        }
        binding.vacanciesRecyclerView.addItemDecoration(itemDecoration)

        viewModel.vacancies.observe(viewLifecycleOwner) { vacancies ->
            vacanciesAdapter.vacancies = vacancies
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun sendBrowserIntent(link: String) {
        if (URLUtil.isValidUrl(link)) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
    }
}