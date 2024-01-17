package com.example.foodapplicationkot.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapplicationkot.DetailActivity
import com.example.foodapplicationkot.Model.MenusItem
import com.example.foodapplicationkot.databinding.ViewMenuBinding


class MenuAdapter(
    private val menuItems: List<MenusItem>,
    private val requireContext: Context): RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ViewMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: ViewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }


            }
        }

        private fun openDetailActivity(position: Int) {

            val menuItem = menuItems[position]
            val intent = Intent(requireContext, DetailActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemIngredient", menuItem.foodIngredient)
                putExtra("MenuItemPrice", menuItem.foodPrice)
            }

            requireContext.startActivity(intent)

        }


        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menufoodname.text = menuItem.foodName
                menuprice.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)

            }
        }

    }



//    interface OnClickListener{
//        fun onItemClick(position: Int)
//
//    }
}


