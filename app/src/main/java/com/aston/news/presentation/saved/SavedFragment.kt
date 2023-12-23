package com.aston.news.presentation.saved

import android.os.Bundle
import android.view.View
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
import com.aston.news.databinding.SavedFragmentBinding
import com.aston.news.presentation.adapters.headlines.HeadlinesAdapter
import com.aston.news.presentation.article.ArticleFragment
import kotlinx.coroutines.launch

class SavedFragment: Fragment(R.layout.saved_fragment) {

    private val binding by viewBinding(SavedFragmentBinding::bind)
    private val viewModel by viewModels<SavedViewModel> {
        Factory {
            (requireActivity().application as NewsApp)
                .getSavedComponent()
                .getViewModel()
        }
    }
    private lateinit var adapter: HeadlinesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState().collect { state ->
                    adapter.submitList(state.items)
                }
            }
        }

    }

    private fun setupRecycler() {
        adapter = HeadlinesAdapter(){ item ->
            val fragment = ArticleFragment()
            fragment.arguments = bundleOf(
                "article" to item.article
            )
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.content.adapter = adapter
        binding.content.layoutManager = LinearLayoutManager(requireContext())
        binding.content.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

}