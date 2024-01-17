package com.example.foodapplicationkot

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.foodapplicationkot.Model.CartItems
import com.example.foodapplicationkot.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private var foodName: String ?= null
    private var foodImage: String ?= null
    private var foodDescription: String ?= null
    private var foodIngredient: String ?= null
    private var foodPrice: String ?= null
    private lateinit var auth: FirebaseAuth
    //private lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailFoodName.text = foodName
            detailDescription.text = foodDescription
            detailIngredient.text = foodIngredient
            Glide.with(this@DetailActivity).load(Uri.parse(foodImage)).into(detailFoodImage)


        }


        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addItemButton.setOnClickListener{
          addItemToCart()

        }
    }

    private fun addItemToCart() {
       val database:DatabaseReference = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""

        val cartItem = CartItems(foodName.toString(), foodPrice.toString(), foodDescription.toString(), foodImage.toString(), foodQuantity = 1 )

        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Items added into cart successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this, "Item Not added", Toast.LENGTH_SHORT).show()
        }
    }
}
