package com.example.testonlineshop.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.testonlineshop.R
import com.example.testonlineshop.databinding.FragmentRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class RootFragment : Fragment() {
    private lateinit var binding: FragmentRootBinding
    private lateinit var navRootController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRootBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navRootHostFragment =
            childFragmentManager.findFragmentById(R.id.root_container) as NavHostFragment

        navRootController = navRootHostFragment.navController

        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navigateTo(PageOneFragment())
                }
                R.id.favorites -> {
                    Toast.makeText(requireContext(), "Navigate to Favorites", Toast.LENGTH_LONG).show()
                }
                R.id.cart -> {
                    Toast.makeText(requireContext(), "Navigate to Shopping Cart", Toast.LENGTH_LONG).show()
                }
                R.id.chat -> {
                    Toast.makeText(requireContext(), "Navigate to Chat", Toast.LENGTH_LONG).show()
                }
                R.id.profile -> {
                    navigateTo(ProfileFragment())
                }
            }
            true
        }


    }

    private fun navigateTo(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.root_container, fragment).commit()
    }
}