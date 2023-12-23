package com.aston.news.presentation.headlines

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aston.news.Constants.LIMIT
import com.aston.news.R
import com.aston.news.application.NewsApp
import com.aston.news.databinding.HeadlinesFragmentBinding
import com.aston.news.domain.Categories
import com.aston.news.domain.Source
import com.aston.news.extension.ui.addOnTabSelectedListener
import com.aston.news.extension.ui.visibleOrGone
import com.aston.news.extension.wrapInAsyncTask
import com.aston.news.presentation.adapters.headlines.HeadlineItem
import com.aston.news.presentation.adapters.headlines.HeadlinesAdapter
import com.aston.news.presentation.article.ArticleFragment
import com.aston.news.presentation.filters.FilterFragment
import com.aston.news.utills.PaginationTool
import com.aston.news.utills.PagingListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class HeadlinesFragment: MvpAppCompatFragment(), HeadlinesViewContract, SearchView.OnQueryTextListener {

    private lateinit var binding: HeadlinesFragmentBinding
    private lateinit var adapter: HeadlinesAdapter
    private var pagingSubscriber: Disposable? = null

    private val source: Source? by lazy { arguments?.getParcelable("source") }

    @Inject
    @InjectPresenter
    @get:ProvidePresenter
    lateinit var presenter: HeadlinesPresenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as NewsApp)
            .getAppComponent()
            .getHeadlinesComponent()
            .inject(this)

        source?.let {
            presenter.setSource(it.id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return HeadlinesFragmentBinding.inflate(inflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerContent()
        setupTabLayout()
        setupPagination()
        setupAppBar()

        binding.tabsFilter.visibleOrGone(source == null)

        binding.swipe.setOnRefreshListener { presenter.fetchData() }
    }

    private fun setupPagination() {

        val pagingListener = object : PagingListener {
            override fun <T> onNextPage(offset: Int): Observable<T> {
                return presenter.fetchDataObserver(offset) as Observable<T>
            }

        }
        val paginationTool = PaginationTool
            .buildPagingObservable<List<HeadlineItem>>(binding.content, pagingListener)
            .setLimit(LIMIT)
            .build()

        pagingSubscriber = paginationTool
            .pagingObservable
            .wrapInAsyncTask()
            .subscribe ({adapter.submitList(it.toList())}, {})

    }

    override fun onDestroyView() {
        super.onDestroyView()
        pagingSubscriber?.dispose()
    }

    private fun setupTabLayout() {
        binding.tabsFilter.addOnTabSelectedListener { tab ->
            val type = when (tab.position) {
                0 -> {
                    Categories.GENERAL
                }

                1 -> {
                    Categories.BUSINESS
                }

                2 -> {
                    Categories.SCIENCE
                }

                else -> null
            }

            type?.let { presenter.setFilter(it) }
        }
    }

    private fun setupRecyclerContent() {
        adapter = HeadlinesAdapter() { item ->
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

    override fun setData(items: List<HeadlineItem>) {
        adapter.submitList(items)
        binding.swipe.isRefreshing = false
    }

    override fun showContentProgressBar() {
        binding.contentProgressBar.visibility = View.VISIBLE
    }

    override fun hideContentProgressBar() {
        binding.contentProgressBar.visibility = View.INVISIBLE
    }

    private fun setupAppBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolBar)
        source?.let {
            binding.toolBar.title = it.name
            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val count = presenter.getFiltersCount()
        if (count > 0) {
            val badgeDrawable = BadgeDrawable.create(requireContext())
            badgeDrawable.number = presenter.getFiltersCount()
            BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.toolBar, R.id.filter_item_menu)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.headline_app_bar_menu, menu)
        val searchItem = menu.findItem(R.id.search_item_menu)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.setOnCloseListener{
            presenter.setQuery("")
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
        presenter.setQuery(text.orEmpty())
        return true
    }

    override fun onQueryTextChange(text: String?): Boolean {
        return true
    }

}