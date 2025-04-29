package com.example.kurirkilat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kurirkilat.R
import com.example.kurirkilat.model.OrderItem

class OrderAdapter(private val orderList: List<OrderItem>) :
        RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

        inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOrderIcon: ImageView = itemView.findViewById(R.id.imgOrderIcon)
        val tvOrderTitle: TextView = itemView.findViewById(R.id.tvOrderTitle)
        val tvOrderStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
        val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_order_info, parent, false)
        return OrderViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.imgOrderIcon.setImageResource(order.iconResId)
        holder.tvOrderTitle.text = order.title
        holder.tvOrderStatus.text = order.status
        holder.tvOrderDate.text = order.date
        }

        override fun getItemCount(): Int = orderList.size
        }
