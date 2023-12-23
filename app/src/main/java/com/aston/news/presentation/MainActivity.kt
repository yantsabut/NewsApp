package com.aston.news.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aston.news.R
import com.aston.news.databinding.ActivityMainBinding
import com.aston.news.presentation.headlines.HeadlinesFragment
import com.aston.news.presentation.saved.SavedFragment
import com.aston.news.presentation.sources.SourcesFragment
import com.aston.news.services.ClearWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val workRequest = PeriodicWorkRequestBuilder<ClearWorker>(10, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext)
            .enqueue(workRequest)

        binding.bottomNavBar.setOnItemSelectedListener { menuitem ->
            when (menuitem.itemId) {
                R.id.headlines -> { openFragment(HeadlinesFragment()) }
                R.id.saved -> { openFragment(SavedFragment()) }
                R.id.sources -> { openFragment(SourcesFragment()) }
            }

            return@setOnItemSelectedListener true
        }
        openFirstFragment()
    }

    private fun openFirstFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HeadlinesFragment())
            .commit()
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}