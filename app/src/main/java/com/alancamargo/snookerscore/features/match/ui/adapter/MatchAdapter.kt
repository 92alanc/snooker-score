package com.alancamargo.snookerscore.features.match.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.alancamargo.snookerscore.core.ui.adapter.DefaultDiffCallback
import com.alancamargo.snookerscore.databinding.ItemMatchBinding
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch

class MatchAdapter(
    private val onItemClick: (UiMatch) -> Unit
) : ListAdapter<UiMatch, MatchViewHolder>(DefaultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMatchBinding.inflate(inflater, parent, false)

        return MatchViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = getItem(position)
        holder.bindTo(match)
    }

}
