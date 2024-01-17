package com.example.foodapplicationkot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodapplicationkot.Fragment.Notification_Bottom_Fragment
import com.example.foodapplicationkot.databinding.ActivityMainBinding
import com.example.foodapplicationkot.databinding.NotificationItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fragmentContainerView)
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(NavController)

        binding.NotificationButton.setOnClickListener {
            val bottomSheetDialog  =Notification_Bottom_Fragment()
           bottomSheetDialog.show(supportFragmentManager, "Test")
        }

    }
}
