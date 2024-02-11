package com.example.adminwaveoffoods.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffoods.databinding.DeliveryItemBinding

class DeliveryAdapter(private val customerNames: ArrayList<String>, private val moneyStatus: ArrayList<String>): RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {
    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                status.text = moneyStatus[position]
                val colorMap = mapOf(
                    "Received" to Color.GREEN,
                    "Not Received" to Color.RED,
                    "Pending" to Color.GRAY
                )
                status.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }
}