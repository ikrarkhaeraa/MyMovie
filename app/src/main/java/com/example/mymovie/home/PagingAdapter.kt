package com.example.mymovie.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.retrofit.response.Movies
import com.example.mymovie.R
import com.example.mymovie.databinding.ItemMovieBinding
import java.text.NumberFormat
import java.util.Locale

class PagingAdapter(private val onMoviesClick: (Movies) -> Unit) :
    PagingDataAdapter<Movies, PagingAdapter.ListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movies>() {
            override fun areItemsTheSame(
                oldItem: Movies,
                newItem: Movies
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Movies,
                newItem: Movies
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movieData = getItem(position)
        if (movieData != null) {
            holder.bind(movieData)
            holder.itemView.setOnClickListener {
                onMoviesClick(movieData)
            }
        }
    }

    class ListViewHolder(var binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(movieData: Movies) {
            val image = "https://image.tmdb.org/t/p/original${movieData.posterPath}"
            binding.apply {
                Glide.with(itemView)
                    .load(image)
                    .centerCrop()
                    .placeholder(R.drawable.image_loading)
                    .into(binding.itemImage)
                binding.movieTitle.text = movieData.title
                val formattedNumber = String.format("%.1f", movieData.voteAverage)
                binding.movieVoteAverage.text = formattedNumber
            }
        }
    }
}