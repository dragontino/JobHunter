package com.jobhunter.app.ui.respond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jobhunter.app.databinding.FragmentRespondBottomSheetBinding

class RespondBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRespondBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRespondBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.respondButton.setOnClickListener { dismiss() }
        binding.addCoverLetter.setOnClickListener {
            RespondDialogFragment.newInstance(null).show(
                requireActivity().supportFragmentManager,
                "RespondDialogFragment"
            )
            dismiss()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}