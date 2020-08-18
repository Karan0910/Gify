package com.company.gify.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.company.gify.R
import com.company.gify.databinding.FragmentFavouriteBinding
import com.company.gify.db.GifDatabase
import com.company.gify.di.DaggerApiComponent
import com.company.gify.ui.adapter.GifAdapter
import com.company.gify.utils.EventObserver
import com.company.gify.viewmodel.FavoriteEventsViewModel
import com.company.gify.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_favourite.fetch_error
import kotlinx.android.synthetic.main.fragment_trending.*


class FavouriteFragment : Fragment() {


    private lateinit var favouriteViewModel: FavouriteViewModel

    private lateinit var searchView: SearchView
    private lateinit var gifAdapter : GifAdapter

    private lateinit var favoriteEventsViewModel: FavoriteEventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        favoriteEventsViewModel = ViewModelProvider(requireActivity()).get(FavoriteEventsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFavouriteBinding.inflate(
            inflater, container, false
        )


        DaggerApiComponent.create().inject(this)

        favouriteViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        var dataBaseInstance = GifDatabase.getDatabasenInstance(requireContext())
        favouriteViewModel?.setInstanceOfDb(dataBaseInstance)

        val view: View = binding.getRoot()



        favouriteViewModel.getGifData()

        gifAdapter = GifAdapter(favouriteViewModel)
        binding.recyclerViewFav.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = gifAdapter
        }

        observeGifList()

        favoriteEventsViewModel.gifFavoriteEvent.observe(viewLifecycleOwner, EventObserver {
            favouriteViewModel.handleGifFavorited(it)
        })
        favoriteEventsViewModel.gifUnfavoriteFromTrendingEvent.observe(viewLifecycleOwner, EventObserver {
            favouriteViewModel.handleGifUnfavorited(it)
        })
        favouriteViewModel.gifUnfavoriteEvent.observe(viewLifecycleOwner, EventObserver {
            favoriteEventsViewModel.handleGifUnfavoritedFromFavorite(it)
        })


        observeIsError()

        return view
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        searchView = menu.findItem(R.id.searchView)?.actionView as SearchView
        searchView.visibility=View.GONE

    }




}