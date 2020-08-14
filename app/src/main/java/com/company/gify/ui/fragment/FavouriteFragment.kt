package com.company.gify.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.company.gify.databinding.FragmentTrendingBinding
import com.company.gify.ui.adapter.TrendingGifAdapter
import com.company.gify.viewmodel.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_trending.*
import javax.inject.Inject


class FavouriteFragment : Fragment() {




    @Inject
    lateinit var gifAdapter: TrendingGifAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTrendingBinding.inflate(
            inflater, container, false
        )


        val view: View = binding.getRoot()
        return view
    }




}