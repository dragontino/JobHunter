package com.jobhunter.app.ui.respond

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.jobhunter.app.databinding.FragmentRespondDialogBinding

class RespondDialogFragment private constructor() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val binding = FragmentRespondDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        val question = arguments?.getString(QUESTION_KEY)
        question?.let {
            binding.coverLetterEditText.setText(question)
        }
        binding.respondButton.setOnClickListener { dismiss() }

        return builder.create()
    }


    companion object {
        fun newInstance(question: String?): RespondDialogFragment {
            return RespondDialogFragment().apply {
                arguments = Bundle().apply { putString(QUESTION_KEY, question) }
            }
        }


        private const val QUESTION_KEY = "Question"
    }
}