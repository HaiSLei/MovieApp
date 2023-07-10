package com.example.movieapp.shared

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.ListItemMovieBinding
import com.example.movieapp.models.Movie


class MovieAdapter(callback: MovieItemCallback = MovieItemCallback()):
    ListAdapter<Movie, MovieAdapter.MovieViewHolder>(callback) {

    val favoriteClickEvent: MutableLiveData<Movie> by lazy {
        MutableLiveData()
    }

    class MovieItemCallback: DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
           return oldItem == newItem
        }
    }

    inner class MovieViewHolder(private val binding: ListItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(movie: Movie) {
                binding.listItemMovieTitle.text = movie.title
                val posterBaseUrl = binding.root.context.getString(R.string.movie_poster_base_url)
                Glide.with(binding.root.context)
                    .load(posterBaseUrl + movie.posterPath)
                    .into(binding.listItemMoviePoster)

                binding.listItemMovieFavorite.setOnClickListener {
                    movie.isLiked = !movie.isLiked
                    favoriteClickEvent.value = movie
                }
                if (movie.isLiked) {
                    binding.listItemMovieFavorite.text = binding.root.context.getString(R.string.favorite_remove)
                }
                else {
                    binding.listItemMovieFavorite.text = binding.root.context.getString(R.string.favorite_add)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ListItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),parent,
            false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}