package com.alancamargo.snookerscore.features.player.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.alancamargo.snookerscore.databinding.ItemPlayerBinding
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

class PlayerViewHolder(
    private val binding: ItemPlayerBinding,
    private val onItemClick: (UiPlayer) -> Unit,
    private val onItemLongClick: (UiPlayer) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(player: UiPlayer) = with(binding) {
        imgAvatar.setImageResource(player.gender.iconRes)
        txtName.text = player.name
        root.setOnClickListener { onItemClick(player) }
        root.setOnLongClickListener {
            onItemLongClick(player)
            true
        }
    }

}
