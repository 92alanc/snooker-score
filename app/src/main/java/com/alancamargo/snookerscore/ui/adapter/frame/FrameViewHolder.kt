package com.alancamargo.snookerscore.ui.adapter.frame

import androidx.recyclerview.widget.RecyclerView
import com.alancamargo.snookerscore.R
import com.alancamargo.snookerscore.databinding.ItemFrameBinding
import com.alancamargo.snookerscore.ui.model.UiFrame

class FrameViewHolder(
    private val binding: ItemFrameBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(frame: UiFrame) = with(binding) {
        val context = root.context
        txtTitle.text = context.getString(R.string.frame_format, frame.positionInMatch)

        txtPlayer1.text = frame.match.player1.name
        txtPlayer1Score.text = context.getString(R.string.score_format, frame.player1Score)
        txtPlayer1HighestBreak.text = context.getString(
            R.string.highest_break_format,
            frame.player1HighestBreak
        )

        txtPlayer2.text = frame.match.player2.name
        txtPlayer2Score.text = context.getString(R.string.score_format, frame.player2Score)
        txtPlayer2HighestBreak.text = context.getString(
            R.string.highest_break_format,
            frame.player2HighestBreak
        )
    }

}
