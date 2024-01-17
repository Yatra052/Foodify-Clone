package com.example.foodapplicationkot.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapplicationkot.Adapter.MenuAdapter
import com.example.foodapplicationkot.Model.MenusItem
import com.example.foodapplicationkot.databinding.FragmentMenuBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MenuBottomFragment :BottomSheetDialogFragment(){
    private lateinit var binding : FragmentMenuBottomBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems : MutableList<MenusItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBottomBinding.inflate(inflater, container, false)


        binding.buttonBack.setOnClickListener{
           dismiss()
        }

        retrieveMenuItems()

        return binding.root
    }

    private fun retrieveMenuItems() {
       database  = FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodSnapshot:DataSnapshot in snapshot.children)
                {
                    val menuItem = foodSnapshot.getValue(MenusItem::class.java)
                    menuItem?.let{
                        menuItems.add(it)
                    }
                    Log.d("Items", "onDataChange: Data Received")
                    setAdapter()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty())
        {
            val adapter: MenuAdapter = MenuAdapter(menuItems,requireContext())
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter
            Log.d("Items", "setAdapter: data set")
        }
        else{
            Log.d("Items", "setAdapter: data not set")

        }

    }


    companion object {

                }

}