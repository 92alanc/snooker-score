package com.alancamargo.snookerscore.ui.adapter.match

import androidx.recyclerview.widget.RecyclerView
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.core.ui.formatDateTime
import com.alancamargo.snookerscore.databinding.ItemMatchBinding
import com.alancamargo.snookerscore.ui.model.UiMatch

class MatchViewHolder(
    private val binding: ItemMatchBinding,
    private val onItemClick: (UiMatch) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(match: UiMatch) = with(binding) {
        val context = root.context
        txtDateTime.text = match.dateTime.formatDateTime()

        val playersText = context.getString(
            R.string.players_format,
            match.player1.name,
            match.player2.name
        )
        txtPlayers.text = playersText

        val framesText = context.resources.getQuantityString(
            R.plurals.frames_plural,
            match.numberOfFrames,
            match.numberOfFrames
        )
        txtFrames.text = framesText

        root.setOnClickListener { onItemClick(match) }
    }

}
