package com.example.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.shared.MovieAdapter
import com.example.movieapp.utilities.checkFavorite
import com.example.movieapp.utilities.getMap

class HomeFragment : Fragment() {

    private val nowPlayingAdapter by lazy {
        MovieAdapter()
    }

    private val favoriteAdapter by lazy {
        MovieAdapter()
    }

    private val homeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

//    private val favoriteViewModel by lazy {
//        ViewModelProvider(this)[HomeViewModel::class.java]
//    }

    private var binding : FragmentHomeBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.nowPlayingRecyclerView?.adapter = nowPlayingAdapter

        binding?.favoriteRecyclerView?.adapter = favoriteAdapter

        val sp = requireContext().getSharedPreferences("favorite", Context.MODE_PRIVATE)


        nowPlayingAdapter.favoriteClickEvent.observe(viewLifecycleOwner) {
            homeViewModel.addToFavorite(it)
        }

        favoriteAdapter.favoriteClickEvent.observe(viewLifecycleOwner) {
            homeViewModel.addToFavorite(it)
        }

        homeViewModel.nowPlayingLiveData.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                binding?.refreshButton?.visibility = View.INVISIBLE

                it.body()?.results?.let { movies ->
                    val favoriteMap = getMap("fa",sp)
                    val checkedFavoriteMovies = checkFavorite(favoriteMap,movies)
                    nowPlayingAdapter.submitList(checkedFavoriteMovies)
                    nowPlayingAdapter.notifyDataSetChanged()
                }
            }
            else {
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
                binding?.refreshButton?.visibility = View.VISIBLE
            }
        }

        homeViewModel.getNowPlayingMovies()

        val favoriteMap = getMap("fa",sp)
        val list = ArrayList(favoriteMap.values)
        favoriteAdapter.submitList(list)

        homeViewModel.favoriteMovieLiveData.observe(viewLifecycleOwner) {
            favoriteAdapter.submitList(it)
        }

        binding?.refreshButton?.setOnClickListener {
            homeViewModel.getNowPlayingMovies()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }



}
