package com.example.adminwaveoffoods.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adminwaveoffoods.databinding.PendingOrdersItemBinding

class PendingOrderAdapter(private val customerNames: ArrayList<String>, private val quantitys: ArrayList<String>, private val foodImage: ArrayList<Int>, private val context: Context): RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
    inner class PendingOrderViewHolder(private val binding: PendingOrdersItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                quantityAmt.text = quantitys[position]
                orderFoodImageView.setImageResource(foodImage[position])
                acceptBtn.apply {
                    if(!isAccepted) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            Toast.makeText(context,"Order is Accepted",Toast.LENGTH_SHORT).show()
                        } else {
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            Toast.makeText(context,"Order is Dispatched",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingOrdersItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }
}