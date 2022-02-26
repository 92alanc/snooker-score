package com.alancamargo.snookerscore.core.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.alancamargo.snookerscore.core.arch.extensions.args
import com.alancamargo.snookerscore.core.arch.extensions.putArguments
import com.alancamargo.snookerscore.databinding.DialogueBinding
import kotlinx.parcelize.Parcelize

class Dialogue : DialogFragment() {

    private var _binding: DialogueBinding? = null
    private val binding get() = _binding!!

    private val args by args<Args>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogueBinding.inflate(inflater, container, false)
        return binding.root
    }

    @Parcelize
    data class Args(
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int,
        @DrawableRes val illustrationRes: Int? = null,
        val primaryButton: ButtonData,
        val secondaryButton: ButtonData? = null
    ) : Parcelable

    @Parcelize
    data class ButtonData(
        @StringRes val textRes: Int,
        val onClick: () -> Unit
    ) : Parcelable

    companion object {
        fun newInstance(args: Args): Dialogue = Dialogue().putArguments(args)
    }

}
