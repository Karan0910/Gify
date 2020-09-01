package com.company.gify.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.company.gify.R
import com.company.gify.databinding.FragmentTrendingBinding
import com.company.gify.db.GifDatabase
import com.company.gify.di.DaggerApiComponent
import com.company.gify.ui.adapter.GifAdapter
import com.company.gify.utils.EventObserver
import com.company.gify.ui.viewmodel.FavoriteEventsViewModel
import com.company.gify.ui.viewmodel.TrendingViewModel
import kotlinx.android.synthetic.main.fragment_trending.*


class TrendingFragment : Fragment() {

    private lateinit var trendingViewModel: TrendingViewModel
    private lateinit var searchView: SearchView
    private lateinit var gifAdapter: GifAdapter
    private lateinit var favoriteEventViewModel: FavoriteEventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteEventViewModel =
            ViewModelProvider(requireActivity()).get(FavoriteEventsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = FragmentTrendingBinding.inflate(
            inflater, container, false
        )
        val view: View = binding.getRoot()

        init()
        initViews(binding)

        observeLiveData()

        setHasOptionsMenu(true)

        return view
    }

    private fun init() {
        DaggerApiComponent.create().inject(this)
        trendingViewModel = ViewModelProvider(this).get(TrendingViewModel::class.java)

        val dataBaseInstance = GifDatabase.getDatabasenInstance(requireContext())
        trendingViewModel.setInstanceOfDb(dataBaseInstance)

        trendingViewModel.onRefresh()
    }

    private fun initViews(binding: FragmentTrendingBinding) {
        binding.mainSwipeRefreshLayout.setOnRefreshListener {
            binding.mainSwipeRefreshLayout.isRefreshing = false
            trendingViewModel.onRefresh()
        }

        gifAdapter = GifAdapter(trendingViewModel)
        binding.recyclerViewTrending.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = gifAdapter
        }

        binding.recyclerViewTrending.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    trendingViewModel.fetchGifs()
                }
            }
        })
    }

    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeGifList()
        observerAddRemoveEvents()
    }

    private fun observeInProgress() {
        trendingViewModel.inProgressLD.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    fetch_error.visibility = View.GONE
                    recycler_view_trending.visibility = View.GONE
                    fetch_progress.visibility = View.VISIBLE
                } else {
                    fetch_progress.visibility = View.GONE
                }
            }
        })

        trendingViewModel.belowInProgressLD.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    progressbar.visibility = View.VISIBLE
                } else {
                    progressbar.visibility = View.GONE
                }
            }
        })
    }

    private fun observeIsError() {
        trendingViewModel.isErrorLD.observe(viewLifecycleOwner, Observer { isError ->
            isError.let { fetch_error.visibility = if (it) View.VISIBLE else View.GONE }
        })
    }

    private fun observeGifList() {
        trendingViewModel.gifListLD.observe(viewLifecycleOwner, Observer { gifList ->
            gifList.let {
                recycler_view_trending.visibility = View.VISIBLE
                gifAdapter.setUpGifs(it)
            }
        })
    }

    private fun observerAddRemoveEvents() {
        favoriteEventViewModel.gifUnfavoriteFromFavoriteEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                trendingViewModel.handleGifUnfavorited(it)
            })
        trendingViewModel.gifFavoriteEvent.observe(viewLifecycleOwner, EventObserver {
            favoriteEventViewModel.handleGifFavorited(it)
        })
        trendingViewModel.gifUnfavoriteEvent.observe(viewLifecycleOwner, EventObserver {
            favoriteEventViewModel.handleGifUnfavoriteFromTrending(it)
        })

        trendingViewModel.closeSearchEvent.observe(viewLifecycleOwner, EventObserver {
            searchView.setQuery("",false)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        searchView = menu.findItem(R.id.searchView)?.actionView as SearchView
        searchView.queryHint = "Search all the GIFs"
        configureSearch(searchView)
    }

    private fun configureSearch(searchView: SearchView) {

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Log.d("TAG", "onClose: ")
                return false
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                trendingViewModel.handleSearchQuery(query)

                return false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}