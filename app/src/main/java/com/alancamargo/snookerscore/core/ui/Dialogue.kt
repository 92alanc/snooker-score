package com.alancamargo.snookerscore.core.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.databinding.DialogueBinding
import kotlinx.parcelize.Parcelize

class Dialogue : DialogFragment() {

    private var _binding: DialogueBinding? = null
    private val binding get() = _binding!!

    @StringRes var titleRes: Int = R.string.empty
    @StringRes var messageRes: Int = R.string.empty
    @DrawableRes var illustrationRes: Int? = null
    var primaryButton: ButtonData = ButtonData()
    var secondaryButton: ButtonData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        isCancelable = false
    }

    private fun setUpUi() = with(binding) {
        setUpTexts()
        setUpIllustration()
        setUpPrimaryButton()
        setUpSecondaryButton()
    }

    private fun DialogueBinding.setUpTexts() {
        txtTitle.setText(titleRes)
        txtMessage.setText(messageRes)
    }

    private fun DialogueBinding.setUpIllustration() {
        illustrationRes?.let(imgIllustration::setImageResource) ?: run {
            imgIllustration.isVisible = false
        }
    }

    private fun DialogueBinding.setUpPrimaryButton() {
        btPrimary.setText(primaryButton.textRes)
        btPrimary.setOnClickListener {
            primaryButton.onClick()
            if (primaryButton.dismissOnClick) {
                dismiss()
            }
        }
    }

    private fun DialogueBinding.setUpSecondaryButton() {
        secondaryButton?.let {
            btSecondary.setText(it.textRes)
            btSecondary.setOnClickListener { _ ->
                it.onClick()
                if (it.dismissOnClick) {
                    dismiss()
                }
            }
        } ?: run {
            btSecondary.isVisible = false
        }
    }

    @Parcelize
    data class ButtonData(
        @StringRes var textRes: Int = R.string.empty,
        var dismissOnClick: Boolean = true,
        var onClick: () -> Unit = { }
    ) : Parcelable

}
