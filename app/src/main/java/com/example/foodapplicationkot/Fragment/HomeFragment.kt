package com.example.foodapplicationkot.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapplicationkot.Adapter.MenuAdapter
import com.example.foodapplicationkot.Model.MenusItem
import com.example.foodapplicationkot.R
import com.example.foodapplicationkot.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var menuItems: MutableList<MenusItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewallmenu.setOnClickListener{
            val bottomSheetDialog = MenuBottomFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")

        }

        retrieveAndDisplayPopularItem()



        return binding.root
    }

    private fun retrieveAndDisplayPopularItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot:DataSnapshot in snapshot.children)
                {
                    val menuItem = foodSnapshot.getValue(MenusItem::class.java)
                     menuItem?.let {
                         menuItems.add(it)
                     }

                    randomPopularItems()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun randomPopularItems() {
       val index  = menuItems.indices.toList().shuffled()
        val numItemsToShow = 6
        val subsetMenuItems= index.take(numItemsToShow).map {
            menuItems[it]
        }

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenusItem>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())
        binding.recycle.layoutManager = LinearLayoutManager(requireContext())
        binding.recycle.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.t1,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.b2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.b3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.t4, ScaleTypes.FIT))


        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
               val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
              Toast.makeText(requireContext(), itemMessage,Toast.LENGTH_SHORT).show()


            }
        })



    }




}