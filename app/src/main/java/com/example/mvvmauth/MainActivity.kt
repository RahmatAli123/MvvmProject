package com.example.mvvmauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mvvmauth.Fragment.CardFragment
import com.example.mvvmauth.Fragment.HomeFragment
import com.example.mvvmauth.Fragment.OrderFragment
import com.example.mvvmauth.Fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonNavigation= findViewById<BottomNavigationView>(R.id.bottom_NavigationView)
        homeFragment(HomeFragment())

        buttonNavigation.setOnItemSelectedListener { item ->

            if (item.itemId== R.id.home_item) {
                homeFragment(HomeFragment())
                return@setOnItemSelectedListener true
            }

            else if (item.itemId== R.id.card_item) {
                homeFragment(CardFragment())
                return@setOnItemSelectedListener true
            }

            else if (item.itemId== R.id.order_item) {
                homeFragment(OrderFragment())
                return@setOnItemSelectedListener true
            }

            else if (item.itemId== R.id.profile_item) {
                homeFragment(ProfileFragment())
                return@setOnItemSelectedListener true
            }

            else{
                return@setOnItemSelectedListener false

            }
        }
    }

    private fun homeFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,fragment)
        transaction.commit()
    }


}
