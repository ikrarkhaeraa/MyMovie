package com.example.mymovie.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.core.SealedClass
import com.example.core.retrofit.response.DetailMovieResponse
import com.example.mymovie.R
import com.example.mymovie.databinding.FragmentDetailBinding
import com.example.mymovie.databinding.FragmentHomeBinding
import com.example.mymovie.home.HomeViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val model: DetailViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var movieId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: androidx.appcompat.widget.Toolbar = binding.topAppBar

        val navigationIcon: View = toolbar

        navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieId = args.id
            model.getDetailProductData(movieId)
            model.movieDetail.collect {
                when (it) {
                    is SealedClass.Loading -> {
                        binding.detailFragment.visibility = GONE
                        showLoading(true)
                    }
                    is SealedClass.Success -> {
                        showLoading(false)
                        binding.detailFragment.visibility = VISIBLE
                        val data = it.data
                        settingUi(data)
                    }
                    is SealedClass.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = VISIBLE
        } else {
            binding.progressBar.visibility = GONE
        }
    }

    private fun settingUi(data: DetailMovieResponse) {
        val image = "https://image.tmdb.org/t/p/original${data.posterPath}"
        Glide.with(requireContext()).load(image).centerCrop().into(binding.itemImage)
        binding.movieTitle.text = data.title
        binding.movieTagline.text = data.tagline

        val formattedNumber = String.format("%.1f", data.voteAverage)
        binding.movieAverage.text = formattedNumber
        binding.movieOverview.text = data.overview
        binding.releasedDate.text = data.releaseDate
        binding.status.text = data.status

        if (data.adult) {
            binding.adult.text = getString(R.string.yes)
        } else {
            binding.adult.text = getString(R.string.no)
        }
        if (data.video) {
            binding.video.text = getString(R.string.yes)
        } else {
            binding.video.text = getString(R.string.no)
        }

        for (i in data.genres.indices) {
            val chip = Chip(requireActivity(), null)
            chip.text = data.genres[i].name
            chip.tag = i
            binding.chipGroup.addView(chip)
        }

    }

}