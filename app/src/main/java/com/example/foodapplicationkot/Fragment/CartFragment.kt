package com.example.foodapplicationkot.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapplicationkot.Adapter.CartAdapter
import com.example.foodapplicationkot.Model.CartItems
import com.example.foodapplicationkot.PayOutActivity
import com.example.foodapplicationkot.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var foodNames : MutableList<String>
    private lateinit var foodPrices : MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImagesUri : MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity : MutableList<Int>
    private lateinit var  cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        retrieveCartItems()


        binding.proceedButton.setOnClickListener {

           getOrderItemsDetail()
//         val intent = Intent(requireContext(), PayOutActivity::class.java)
//            startActivity(intent)
        }

        return binding.root
    }



    private fun getOrderItemsDetail() {
        val orderIdReference:DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        val foodQuantities = cartAdapter.getUpdatedItemsQuantities()
        //getUpdatedItemsQuantities()

           orderIdReference.addListenerForSingleValueEvent(object:ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children)
                {
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)

                    orderItems?.foodName?.let{ foodName.add(it) }
                    orderItems?.foodPrice?.let{ foodPrice.add(it) }
                    orderItems?.foodDescription?.let{ foodDescription.add(it) }
                    orderItems?.foodImage?.let{ foodImage.add(it) }
                    orderItems?.foodIngredient?.let{ foodIngredient.add(it)
                         }

                }

                   orderNow(foodName, foodPrice, foodDescription, foodImage, foodIngredient,
                       foodQuantities)
               }

               override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(requireContext(), "Order making Failed. Please Try Again", Toast.LENGTH_SHORT).show()

               }

           } )

    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>,
    ) {
        if (isAdded && context !=null)
        {
           val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("FoodItemName", foodName as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
            intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantities" , foodQuantities as ArrayList<Int>)
            startActivity(intent)

        }
    }

    private fun retrieveCartItems() {
         database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
       val foodReference:DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        foodIngredients = mutableListOf()
        quantity=  mutableListOf()

        //fetch data
        foodReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             for (foodSnapshot in snapshot.children)
             {
                 val cartItems = foodSnapshot.getValue(CartItems::class.java)
                 cartItems?.foodName?.let{
                     foodNames.add(it)
                 }
                 cartItems?.foodPrice?.let {
                     foodPrices.add(it)
                 }
                 cartItems?.foodDescription?.let {
                     foodDescriptions.add(it)
                 }

                 cartItems?.foodImage?.let {
                     foodImagesUri.add(it)
                 }

                 cartItems?.foodQuantity?.let {
                    quantity.add(it)
                 }
                 cartItems?.foodIngredient?.let{
                     foodIngredients.add(it)
                 }

                 setAdapter()

             }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "This data not fetched", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setAdapter() {
       cartAdapter = CartAdapter(requireContext(), foodNames, foodPrices, foodDescriptions, foodImagesUri, quantity, foodIngredients)
        binding.cartrecyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartrecyclerview.adapter =cartAdapter
    }

    companion object {

    }
}


