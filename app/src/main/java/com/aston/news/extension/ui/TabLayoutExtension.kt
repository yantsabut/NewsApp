package com.aston.news.extension.ui

import com.google.android.material.tabs.TabLayout

fun TabLayout.addOnTabSelectedListener(function: (TabLayout.Tab) -> Unit) {
    addOnTabSelectedListener(
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                function.invoke(tab)
            }
        }
    )
}