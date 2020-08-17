package com.company.gify.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.company.gify.databinding.FragmentFavouriteBinding
import com.company.gify.databinding.FragmentTrendingBinding
import com.company.gify.db.GifDatabase
import com.company.gify.db.entities.GifData
import com.company.gify.di.DaggerApiComponent
import com.company.gify.model.Gif
import com.company.gify.ui.adapter.FavGifAdapter
import com.company.gify.ui.adapter.TrendingGifAdapter
import com.company.gify.viewmodel.FavouriteViewModel
import com.company.gify.viewmodel.TrendingViewModel
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_trending.*
import javax.inject.Inject


class FavouriteFragment : Fragment() {


    @Inject
    lateinit var gifAdapter: FavGifAdapter

    private lateinit var favouriteViewModel: FavouriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


        binding.recyclerViewFav.apply {
            layoutManager = GridLayoutManager(context, 2)
            gifAdapter.viewModel = favouriteViewModel
            adapter = gifAdapter
        }

        observeGifList()
        return view
    }

    private fun observeGifList() {
        favouriteViewModel.gifListLD.observe(viewLifecycleOwner, Observer { gifList ->
            gifList.let {
                recycler_view_fav.visibility = View.VISIBLE
                gifAdapter.setUpGifs(it)
            }
        })
    }




}