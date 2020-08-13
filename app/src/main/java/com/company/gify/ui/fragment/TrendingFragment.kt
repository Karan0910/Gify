package com.company.gify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.gify.R
import com.company.gify.databinding.FragmentTrendingBinding
import com.company.gify.di.DaggerApiComponent
import com.company.gify.ui.adapter.TrendingGifAdapter
import com.company.gify.viewmodel.TrendingViewModel
import kotlinx.android.synthetic.main.fragment_trending.*
import kotlinx.android.synthetic.main.fragment_trending.view.*
import javax.inject.Inject


class TrendingFragment : Fragment() {

    @Inject
    lateinit var gifAdapter: TrendingGifAdapter

    private lateinit var trendingViewModel: TrendingViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        trendingViewModel =  ViewModelProvider(this).get(TrendingViewModel::class.java)
        val binding  = FragmentTrendingBinding.inflate(
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
            layoutManager = GridLayoutManager(context,2)
            adapter = gifAdapter
        }

        observeLiveData()

        return view
    }


    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeGifList()
    }


    private fun observeGifList()
    {
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
    }


    private fun observeIsError() {
        trendingViewModel.isErrorLD.observe(viewLifecycleOwner, Observer { isError ->
            isError.let { fetch_error.visibility = if (it) View.VISIBLE else View.GONE }
        })
    }

}