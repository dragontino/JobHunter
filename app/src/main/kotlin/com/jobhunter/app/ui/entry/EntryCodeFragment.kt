package com.jobhunter.app.ui.entry

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.jobhunter.app.R
import com.jobhunter.app.databinding.FragmentEntryCodeBinding
import com.jobhunter.app.ui.codeNumbersDelegate
import com.jobhunter.app.utils.applyWindowInsets
import com.jobhunter.app.utils.createSpacerDrawable

class EntryCodeFragment : Fragment() {

    private var _binding: FragmentEntryCodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EntryCodeViewModel>()

    private val args by navArgs<EntryCodeFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.applyWindowInsets(WindowInsetsCompat.Type.statusBars())
        binding.sendCodeToEmailTextView.text = getString(R.string.send_code_to_email, args.email)
        updateEnabledInSubmitButton(false)

        val codeAdapter = ListDelegationAdapter(
            codeNumbersDelegate { text, position ->
                viewModel.updateCodeNumber(number = text, position = position)
            }
        )

        binding.codeRecyclerView.adapter = codeAdapter
        val itemDeclaration = DividerItemDecoration(context, RecyclerView.HORIZONTAL).apply {
            setDrawable(createSpacerDrawable(8))
        }
        binding.codeRecyclerView.addItemDecoration(itemDeclaration)

        viewModel.code.observe(viewLifecycleOwner) { code ->
            codeAdapter.items = code.map { it ?: "" }
            updateEnabledInSubmitButton(
                enabled = code.all { it != null }
            )
        }

        binding.submitButton.setOnClickListener {
            val action = EntryCodeFragmentDirections.showMainScreenAction()
            findNavController().navigate(action)
        }
    }


    private fun updateEnabledInSubmitButton(enabled: Boolean) {
        binding.submitButton.apply {
            this.isEnabled = enabled
            val textColor = when {
                enabled -> Color.WHITE
                else -> context.getColor(R.color.grey4)
            }
            setTextColor(textColor)

            val backgroundTint = when {
                enabled -> context.getColor(R.color.blue)
                else -> context.getColor(R.color.dark_blue)
            }
            backgroundTintList = ColorStateList.valueOf(backgroundTint)
        }
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}