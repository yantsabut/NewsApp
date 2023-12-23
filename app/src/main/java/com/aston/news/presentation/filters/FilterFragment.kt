package com.aston.news.presentation.filters

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aston.news.R
import com.aston.news.application.NewsApp
import com.aston.news.base.Factory
import com.aston.news.databinding.FilterFragmentBinding
import com.aston.news.domain.ArticleLanguage
import com.aston.news.domain.ArticleSort
import com.aston.news.domain.Filter
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale

class FilterFragment: Fragment(R.layout.filter_fragment) {

    private val picker: MaterialDatePicker<androidx.core.util.Pair<Long, Long>>
        get() {
            val datePicker = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setTitleText(getString(R.string.select_date))
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .build()

            datePicker.addOnPositiveButtonClickListener {date ->
                viewModel.onTriggerEvent(FilterIntent.SetDate(Pair(date.first, date.second)))
            }

            //clear button not implemented
            //https://github.com/material-components/material-components-android/pull/3145
            datePicker.addOnNegativeButtonClickListener {
                viewModel.onTriggerEvent(FilterIntent.SetDate(null))
            }

            return datePicker
        }

    private val viewModel: FilterViewModel by viewModels {
        Factory {
            (requireActivity().application as NewsApp)
                .getAppComponent()
                .getFilterComponent()
                .getViewModel()
        }
    }

    private val binding by viewBinding(FilterFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToggleButtons()
        setupAppBar()
        setupDateRangePicker()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            updateFilters(state.filter)
        }

        viewModel.onTriggerEvent(FilterIntent.FetchData)
    }

    private fun updateFilters(filter: Filter) {

        filter.language?.let {
            val id = when (it) {
                ArticleLanguage.ENGLISH -> { binding.eng.id }
                ArticleLanguage.RUSSIAN -> { binding.rus.id }
                ArticleLanguage.DEUTSCH -> { binding.deutsch.id }
            }
            binding.toggleLang.check(id)
        }
        updateDateValue(filter.date)

        binding.popular.isChecked = filter.sort == ArticleSort.POPULARITY
        binding.newButton.isChecked = filter.sort == ArticleSort.NEW
        binding.relevant.isChecked = filter.sort == ArticleSort.RELEVANCY

        val drawable = if (filter.date == null) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_calendar_on)
        }
        binding.dateButton.setImageDrawable(drawable)
    }

    private fun setupDateRangePicker() {
        binding.dateButton.setOnClickListener {
            picker.show(childFragmentManager, "calendar")
        }
    }

    private fun setupAppBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolBar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filters_app_bar_menu, menu)
        val item = menu.findItem(R.id.save)
        item.setOnMenuItemClickListener {
            val sort = when (binding.toggleButton.checkedButtonId) {
                binding.popular.id -> { ArticleSort.POPULARITY }
                binding.newButton.id -> { ArticleSort.NEW }
                binding.relevant.id -> { ArticleSort.RELEVANCY }
                else -> null
            }
            val language = when (binding.toggleLang.checkedChipId) {
                binding.rus.id -> { ArticleLanguage.RUSSIAN }
                binding.eng.id -> { ArticleLanguage.ENGLISH }
                binding.deutsch.id -> { ArticleLanguage.DEUTSCH }
                else -> null
            }

            val filter = Filter(
                sort = sort,
                language = language
            )
            viewModel.onTriggerEvent(FilterIntent.SetFilter(filter))
            parentFragmentManager.popBackStack()
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupToggleButtons() = with(binding){

        relevant.addOnCheckedChangeListener { button, isChecked ->
            setupToggleButton(button, isChecked, ArticleSort.RELEVANCY)
        }
        newButton.addOnCheckedChangeListener{ button, isChecked ->
            setupToggleButton(button, isChecked, ArticleSort.NEW)
        }
        popular.addOnCheckedChangeListener{ button, isChecked ->
            setupToggleButton(button, isChecked, ArticleSort.POPULARITY)
        }
    }

    private fun setupToggleButton(button: MaterialButton, isChecked: Boolean, sort: ArticleSort) {
        if (isChecked) {
            button.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_done)
        } else {
            button.icon = null
        }
    }

    private fun updateDateValue(date: Pair<Long, Long>?) {
        if (date == null) {
            binding.dateValue.text = ContextCompat.getString(requireContext(), R.string.choose_date)
            binding.dateValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_light_outline))
            return
        }

        val first = date.first
        val second = date.second

        val secondText = SimpleDateFormat("LLL dd, yyyy", Locale.getDefault()).format(second)
        val firstText = SimpleDateFormat("LLL dd", Locale.getDefault()).format(first)

        binding.dateValue.text = "$firstText-$secondText"
        binding.dateValue.setTextColor(ContextCompat.getColor(requireContext(), R.color.md_theme_light_primary))
    }


}