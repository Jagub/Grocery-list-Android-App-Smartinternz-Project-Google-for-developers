package com.example.grocerylistapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylistapp.R
import com.example.grocerylistapp.database.entity.GroceryItems

class GroceryAdapter (
        var list : List <GroceryItems>,
        val groceryItemClickInterface : GroceryItemClickInterface
        ) : RecyclerView.Adapter <GroceryAdapter.GroceryViewHolder > ( ) {

        inner class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val tv_name = itemView.findViewById<TextView>(R.id.tv_item_name)
                val tv_quantity = itemView.findViewById<TextView>(R.id.tv_item_quantity)
                val tv_price = itemView.findViewById<TextView>(R.id.tv_item_rate)
                val tv_amount = itemView.findViewById<TextView>(R.id.tv_total_amount)
                val iv_delete = itemView.findViewById<ImageView>(R.id.iv_item_delete)
        }

        interface GroceryItemClickInterface {
                fun onItemClick(groceryItems: GroceryItems)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.grocery_rv_item,parent,false)
                return GroceryViewHolder(view)
        }

        override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
                holder.tv_name.text = list.get(position).itemName
                holder.tv_quantity.text = list.get(position).itemQuantity.toString()
                holder.tv_price.text = "Rs. "+list.get(position).itemPrice.toString()
                holder.tv_amount.text = "Rs. " + (list.get(position).itemPrice * list.get(position).itemQuantity).toString()
                holder.iv_delete.setOnClickListener {
                        groceryItemClickInterface.onItemClick(list.get(position))
                }
        }

        override fun getItemCount(): Int {
                return list.size
        }
}

