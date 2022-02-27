package com.alancamargo.snookerscore.core.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val inflater = LayoutInflater.from(context)
        _binding = DialogueBinding.inflate(inflater)
        isCancelable = false

        setUpUi()
        return getDialogue(context)
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

    private fun getDialogue(context: Context): AlertDialog {
        return AlertDialog.Builder(context).setView(binding.root).create().apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    @Parcelize
    data class ButtonData(
        @StringRes var textRes: Int = R.string.empty,
        var dismissOnClick: Boolean = true,
        var onClick: () -> Unit = { }
    ) : Parcelable

}
