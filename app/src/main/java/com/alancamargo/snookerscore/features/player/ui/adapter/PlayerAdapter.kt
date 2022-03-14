package com.alancamargo.snookerscore.features.player.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.alancamargo.snookerscore.core.ui.adapter.DefaultDiffCallback
import com.alancamargo.snookerscore.databinding.ItemPlayerBinding
import com.alancamargo.snookerscore.features.player.ui.model.UiPlayer

class PlayerAdapter(
    private val onItemClick: (UiPlayer) -> Unit,
    private val onItemLongClick: (UiPlayer) -> Unit
) : ListAdapter<UiPlayer, PlayerViewHolder>(DefaultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlayerBinding.inflate(inflater, parent, false)
        return PlayerViewHolder(binding, onItemClick, onItemLongClick)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = getItem(position)
        holder.bindTo(player)
    }

}
