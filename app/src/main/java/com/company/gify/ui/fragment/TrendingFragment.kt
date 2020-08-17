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
import com.company.gify.ui.adapter.TrendingGifAdapter
import com.company.gify.viewmodel.TrendingViewModel
import kotlinx.android.synthetic.main.fragment_trending.*
import javax.inject.Inject


class TrendingFragment : Fragment() {

    @Inject
    lateinit var gifAdapter: TrendingGifAdapter

    private lateinit var trendingViewModel: TrendingViewModel

    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        trendingViewModel = ViewModelProvider(this).get(TrendingViewModel::class.java)


        val binding = FragmentTrendingBinding.inflate(
            inflater, container, false
        )
        val view: View = binding.getRoot()

        DaggerApiComponent.create().inject(this)

        trendingViewModel.onRefresh()

        binding.mainSwipeRefreshLayout.setOnRefreshListener {
            binding.mainSwipeRefreshLayout.isRefreshing = false
            trendingViewModel.onRefresh()
        }

        binding.recyclerViewTrending.apply {
            layoutManager = GridLayoutManager(context, 2)
            gifAdapter.viewModel = trendingViewModel
            adapter = gifAdapter
        }

        binding.recyclerViewTrending.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && dy != 0) {
                    Log.d("TAG", "onScrolled: ")
                    trendingViewModel.fetchGifs()

                }
            }
        })

        var dataBaseInstance = GifDatabase.getDatabasenInstance(requireContext())
        trendingViewModel?.setInstanceOfDb(dataBaseInstance)

        observeLiveData()

        setHasOptionsMenu(true)

        return view
    }


    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeGifList()
    }


    private fun observeGifList() {
        trendingViewModel.gifListLD.observe(viewLifecycleOwner, Observer { gifList ->
            gifList.let {
                recycler_view_trending.visibility = View.VISIBLE
                gifAdapter.setUpGifs(it)
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        searchView = menu.findItem(R.id.searchView)?.actionView as SearchView
        searchView.queryHint = "Search all the GIFs"
        configureSearch(searchView)
    }


    private fun configureSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                trendingViewModel.fetchSearchedGifs(query);

                return false
            }

        })
    }

}