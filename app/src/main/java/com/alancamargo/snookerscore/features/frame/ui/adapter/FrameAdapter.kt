package com.alancamargo.snookerscore.features.frame.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.alancamargo.snookerscore.core.ui.adapter.DefaultDiffCallback
import com.alancamargo.snookerscore.databinding.ItemFrameBinding
import com.alancamargo.snookerscore.features.frame.ui.model.UiFrame

class FrameAdapter : ListAdapter<UiFrame, FrameViewHolder>(DefaultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFrameBinding.inflate(inflater, parent, false)
        return FrameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        val frame = getItem(position)
        holder.bindTo(frame)
    }

}
