package com.alancamargo.snookerscore.core.ui

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.databinding.DialogueBinding

class Dialogue : DialogFragment() {

    private var _binding: DialogueBinding? = null
    private val binding get() = _binding!!

    @StringRes var titleRes: Int = R.string.empty
    @StringRes var messageRes: Int? = null
    @DrawableRes var illustrationRes: Int? = null
    var primaryButton: ButtonData = ButtonData()
    var secondaryButton: ButtonData? = null
    var editText: EditTextData? = null
    var radioButtons: RadioGroupData? = null

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
        setUpEditText()
        setUpRadioGroup()
        setUpPrimaryButton()
        setUpSecondaryButton()
    }

    private fun DialogueBinding.setUpTexts() {
        txtTitle.setText(titleRes)
        messageRes?.let(txtMessage::setText) ?: run { txtMessage.isVisible = false }
    }

    private fun DialogueBinding.setUpIllustration() {
        illustrationRes?.let(imgIllustration::setImageResource) ?: run {
            imgIllustration.isVisible = false
        }
    }

    private fun DialogueBinding.setUpEditText() {
        editText?.let {
            til.setHint(it.hintRes)
        } ?: run {
            til.isVisible = false
        }
    }
    
    private fun DialogueBinding.setUpRadioGroup() {
        radioButtons?.let {
            it.radioButtons.forEach { radioButtonData ->
                val radioButton = RadioButton(requireContext()).also { rb ->
                    rb.id = radioButtonData.id
                    rb.setText(radioButtonData.textRes)
                    val colour = ContextCompat.getColor(radioGroup.context, R.color.green)
                    rb.buttonTintList = ColorStateList.valueOf(colour)
                }
                
                radioGroup.addView(radioButton)
            }
        } ?: run {
            radioGroup.isVisible = false
        }
    }

    private fun DialogueBinding.setUpPrimaryButton() {
        btPrimary.setText(primaryButton.textRes)
        btPrimary.setOnClickListener {
            editText?.onSubmitText?.invoke(edt.text.toString())
            radioButtons?.onSubmitSelection?.invoke(radioGroup.checkedRadioButtonId)
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

    data class ButtonData(
        @StringRes var textRes: Int = R.string.empty,
        var dismissOnClick: Boolean = true,
        var onClick: () -> Unit = { }
    )

    data class EditTextData(
        @StringRes var hintRes: Int = R.string.empty,
        var onSubmitText: (String) -> Unit = { }
    )

    data class RadioGroupData(
        var radioButtons: MutableList<RadioButtonData> = mutableListOf(),
        var onSubmitSelection: (Int) -> Unit = { }
    )

    data class RadioButtonData(
        var id: Int = 0,
        @StringRes var textRes: Int = R.string.empty
    )

}
