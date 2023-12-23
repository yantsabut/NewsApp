package com.aston.news.presentation.sources

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aston.news.R
import com.aston.news.application.NewsApp
import com.aston.news.base.Factory
import com.aston.news.databinding.SourcesFragmentBinding
import com.aston.news.domain.Source
import com.aston.news.presentation.adapters.sources.SourcesAdapter
import com.aston.news.presentation.filters.FilterFragment
import com.aston.news.presentation.headlines.HeadlinesFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.coroutines.launch

class SourcesFragment: Fragment(R.layout.sources_fragment), SearchView.OnQueryTextListener {

    private val binding by viewBinding(SourcesFragmentBinding::bind)
    private val viewModel by viewModels<SourcesViewModel> {
        Factory {
            (requireActivity().application as NewsApp)
                .getSourcesComponent()
                .getViewModel()
        }
    }

    private lateinit var adapter: SourcesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupAppBar()
        binding.swipe.setOnRefreshListener { viewModel.fetchData() }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState().collect{ state ->
                    adapter.submitList(state.items)
                    binding.contentProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.INVISIBLE
                    binding.swipe.isRefreshing = state.isLoading
                }
            }
        }
    }

    private fun setupAppBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolBar)
    }

    private fun setupRecyclerView() {
        adapter = SourcesAdapter { openHeadlines(it) }

        binding.content.adapter = adapter
        binding.content.layoutManager = LinearLayoutManager(requireContext())
        binding.content.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun openHeadlines(source: Source) {
        val fragment = HeadlinesFragment()
        fragment.arguments = bundleOf(
            "source" to source
        )
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.headline_app_bar_menu, menu)
        val searchItem = menu.findItem(R.id.search_item_menu)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnCloseListener{
            viewModel.setQuery("")
            searchView.onActionViewCollapsed()
            true
        }


        val filterItem = menu.findItem(R.id.filter_item_menu)
        filterItem.setOnMenuItemClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, FilterFragment())
                .addToBackStack(null)
                .commit()

            true
        }
    }

    override fun onQueryTextSubmit(text: String?): Boolean {
        viewModel.setQuery(text.orEmpty())
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        viewModel.setQuery(text.orEmpty())
        return true
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val count = viewModel.getFiltersCount()
        if (count > 0) {
            val badgeDrawable = BadgeDrawable.create(requireContext())
            badgeDrawable.number = count
            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.toolBar, R.id.filter_item_menu)
        }
    }

}