package com.company.gify.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.company.gify.databinding.FragmentFavouriteBinding
import com.company.gify.db.GifDatabase
import com.company.gify.di.DaggerApiComponent
import com.company.gify.ui.adapter.GifAdapter
import com.company.gify.utils.EventObserver
import com.company.gify.ui.viewmodel.FavoriteEventsViewModel
import com.company.gify.ui.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_favourite.*


class FavouriteFragment : Fragment() {

    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var gifAdapter: GifAdapter
    private lateinit var favoriteEventsViewModel: FavoriteEventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoriteEventsViewModel =
            ViewModelProvider(requireActivity()).get(FavoriteEventsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFavouriteBinding.inflate(
            inflater, container, false
        )

        init()
        val view: View = binding.getRoot()

        gifAdapter = GifAdapter(favouriteViewModel)
        binding.recyclerViewFav.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = gifAdapter
        }

        observeGifList()
        observeIsError()
        observerAddRemoveEvents()

        return view
    }

    private fun init() {
        DaggerApiComponent.create().inject(this)
        favouriteViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        val dataBaseInstance = GifDatabase.getDatabasenInstance(requireContext())
        favouriteViewModel.setInstanceOfDb(dataBaseInstance)

        favouriteViewModel.getGifData()

    }

    private fun observeIsError() {
        favouriteViewModel.isErrorLD.observe(viewLifecycleOwner, Observer { isError ->
            isError.let { fetch_error.visibility = if (it) View.VISIBLE else View.GONE }
        })
    }

    private fun observeGifList() {
        favouriteViewModel.gifListLD.observe(viewLifecycleOwner, Observer { gifList ->
            gifList.let {
                recycler_view_fav.visibility = View.VISIBLE
                gifAdapter.setUpGifs(it)
            }
        })
    }

    private fun observerAddRemoveEvents() {
        favoriteEventsViewModel.gifFavoriteEvent.observe(viewLifecycleOwner, EventObserver {
            favouriteViewModel.handleGifFavorited(it)
        })
        favoriteEventsViewModel.gifUnfavoriteFromTrendingEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                favouriteViewModel.handleGifUnfavorited(it)
            })
        favouriteViewModel.gifUnfavoriteEvent.observe(viewLifecycleOwner, EventObserver {
            favoriteEventsViewModel.handleGifUnfavoritedFromFavorite(it)
        })
    }

}