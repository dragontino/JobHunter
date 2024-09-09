package com.jobhunter.app.ui.entry

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jobhunter.app.R
import com.jobhunter.app.databinding.FragmentEntryEmailBinding
import com.jobhunter.app.utils.applyWindowInsets
import com.jobhunter.app.utils.hide
import com.jobhunter.app.utils.show

class EntryEmailFragment : Fragment() {

    private var _binding: FragmentEntryEmailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.entryToolbar.applyWindowInsets(WindowInsetsCompat.Type.statusBars())
        updateEnabledInProceedButton(
            enabled = !binding.emailEditText.text.isNullOrBlank()
        )

        binding.emailEditText.apply {
            doAfterTextChanged { text ->
                when {
                    text.isNullOrBlank() -> updateCompoundDrawablesOnEmailEditText(end = null)
                    else -> updateCompoundDrawablesOnEmailEditText(
                        end = getDrawable(R.drawable.sharp_close_24)
                    )
                }

                updateEnabledInProceedButton(enabled = !text.isNullOrBlank())
                updateErrorInEmailEditText(error = false, errorMessage = null)
            }

            onClickToRightDrawableListener {
                setText("")
            }

            setOnEditorActionListener { view, _, _ ->
                view.clearFocus()
                true
            }
        }

        binding.proceedButton.setOnClickListener {
            val email = binding.emailEditText.text!!.toString()
            if (isValidEmail(email)) {
                val action = EntryEmailFragmentDirections.sendEntryCodeAction(email)
                findNavController().navigate(action)
            } else {
                updateErrorInEmailEditText(
                    error = true,
                    errorMessage = getString(R.string.invalid_email_exception)
                )
            }
        }
    }


    private fun getDrawable(@DrawableRes id: Int): Drawable? {
        return AppCompatResources.getDrawable(requireContext(), id)
    }


    private fun updateEnabledInProceedButton(enabled: Boolean) {
        binding.proceedButton.apply {
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


    private inline fun TextView.onClickToRightDrawableListener(crossinline onClick: () -> Unit) {
        setOnTouchListener { _, event ->
            performClick()
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= right - totalPaddingRight) {
                onClick()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }


    private fun TextView.getCompoundDrawable(direction: CompoundDrawableDirection): Drawable? {
        return when (direction) {
            CompoundDrawableDirection.Start -> compoundDrawablesRelative.getOrNull(0)
            CompoundDrawableDirection.End -> compoundDrawablesRelative.getOrNull(2)
            CompoundDrawableDirection.Top -> compoundDrawablesRelative.getOrNull(1)
            CompoundDrawableDirection.Bottom -> compoundDrawablesRelative.getOrNull(3)
        }
    }


    private fun updateCompoundDrawablesOnEmailEditText(
        start: Drawable? = binding.emailEditText
            .getCompoundDrawable(CompoundDrawableDirection.Start),
        end: Drawable? = binding.emailEditText
            .getCompoundDrawable(CompoundDrawableDirection.End),
        top: Drawable? = binding.emailEditText
            .getCompoundDrawable(CompoundDrawableDirection.Top),
        bottom: Drawable? = binding.emailEditText
            .getCompoundDrawable(CompoundDrawableDirection.Bottom)
    ) {
        binding.emailEditText.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                /* start = */ start,
                /* top = */ top,
                /* end = */ end,
                /* bottom = */ bottom
            )
        }
    }


    private fun updateErrorInEmailEditText(error: Boolean, errorMessage: String?) {
        binding.emailEditText.apply {
            val background = when {
                error -> getDrawable(R.drawable.text_field_error)
                else -> getDrawable(R.drawable.text_field)
            }
            setBackgroundDrawable(background)
        }

        binding.emailErrorMessage.apply {
            when (errorMessage) {
                null -> hide()
                else -> {
                    text = errorMessage
                    show()
                }
            }
        }
    }


    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private enum class CompoundDrawableDirection {
        Start,
        End,
        Top,
        Bottom
    }
}