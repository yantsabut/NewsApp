package com.aston.news.presentation.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.aston.news.R
import com.aston.news.application.NewsApp
import com.aston.news.base.Factory
import com.aston.news.databinding.ArticleFragmentBinding
import com.aston.news.domain.Article
import com.aston.news.extension.ui.visibleOrGone
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ArticleFragment: Fragment(R.layout.article_fragment) {

    private val binding by viewBinding(ArticleFragmentBinding::bind)
    private val article: Article? by lazy { requireArguments().getParcelable("article") }
    private val clickableSpan = object : ClickableSpan() {
        override fun onClick(p0: View) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(viewModel.uiState().value.url))
            startActivity(intent)
        }

    }

    private val viewModel by viewModels<ArticleViewModel> {
        Factory {
            (requireActivity().application as NewsApp)
                .getArticleComponentFactory()
                .create(requireNotNull(article))
                .getViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()

        binding.description.movementMethod = LinkMovementMethod.getInstance()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState().collect { state ->
                    binding.title.text = state.title
                    binding.date.text = state.date
                    binding.source.text = state.source

                    updateDescription(state)
                    updateSaveIcon(state.isSaved)

                    Glide.with(requireContext())
                        .load(state.src)
                        .placeholder(R.drawable.news_placeholder)
                        .into(binding.appBarSrc)
                }
            }
        }
    }

    private fun updateDescription(state: DataState) {

        binding.description.text = if (state.description != null) {
            val text = "Ссылка для перехода"
            val ref = "${state.description}\n$text"
            SpannableString(ref).also {
                it.setSpan(clickableSpan, ref.length - text.length, ref.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

        } else {
            null
        }

        updateNoContentVisible(state.description == null)
    }

    private fun updateNoContentVisible(visible: Boolean) {
        binding.imageNoContent.visibleOrGone(visible)
        binding.textNoContent.visibleOrGone(visible)
        binding.description.visibleOrGone(!visible)
    }

    private fun updateSaveIcon(isSaved: Boolean) {
        val icon = if (isSaved) R.drawable.ic_is_saved else R.drawable.ic_favourite
        binding.toolBar.menu.findItem(R.id.save)?.icon = ContextCompat.getDrawable(requireContext(), icon)
    }

    private fun setupAppBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolBar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.collapsingToolbar.title = article?.title ?: " "
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            R.id.save -> {
                viewModel.save()
                updateSaveIcon(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.article_app_bar_menu, menu)
    }

}