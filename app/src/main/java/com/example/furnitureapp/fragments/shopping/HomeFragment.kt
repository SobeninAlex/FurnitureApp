package com.example.furnitureapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.HomeViewpagerAdapter
import com.example.furnitureapp.databinding.FragmentHomeBinding
import com.example.furnitureapp.fragments.categories.AccessoryFragment
import com.example.furnitureapp.fragments.categories.ChairFragment
import com.example.furnitureapp.fragments.categories.CupboardFragment
import com.example.furnitureapp.fragments.categories.FurnitureFragment
import com.example.furnitureapp.fragments.categories.MainCategoryFragment
import com.example.furnitureapp.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewPager() {
        val categoriesFragments = listOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        val viewPagerAdapter = HomeViewpagerAdapter(
            fragments = categoriesFragments,
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle
        )

        binding.viewPagerHome.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome) { tab, position ->
            val array = resources.getStringArray(R.array.tab_titles)
            tab.text = array[position]
        }.attach()
    }

}