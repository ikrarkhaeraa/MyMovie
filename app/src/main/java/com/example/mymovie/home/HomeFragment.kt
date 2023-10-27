package com.example.mymovie.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.core.retrofit.response.Movies
import com.example.mymovie.R
import com.example.mymovie.databinding.FragmentHomeBinding
import com.example.mymovie.detail.DetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val model: HomeViewModel by activityViewModels()
    private val pagingAdapter = PagingAdapter { movie ->
        findNavController().navigate(
            R.id.home_to_detail,
            DetailFragmentArgs(movie.id).toBundle(),
            navOptions = null
        )
    }
    private lateinit var listMovies: PagingData<Movies>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            val userLoginState = model.getUserLoginState().first()
            if (!userLoginState) {
                findNavController().navigate(R.id.main_to_login)
            }
        }

        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_1 -> {
                    model.userLogout()
                    findNavController().navigate(R.id.main_to_login)
                }
            }
            true
        }

        settingAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            sendAndGetData()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { pagingState ->
                val isLoading = pagingState.refresh is LoadState.Loading
                val isError = pagingState.refresh is LoadState.Error
                val isSuccess = pagingState.refresh is LoadState.NotLoading

                hideError()

                if (isLoading) {
                    Log.d("cekHome", "loading")
                    showLoading(true)
                    hideUi()
                } else if (isSuccess) {
                    Log.d("cekHome", "success")
                    showLoading(false)
                    showUi()
                } else if (isError) {
                    Log.d("cekHome", "error")
                    showLoading(false)
                    val error = (pagingState.refresh as LoadState.Error).error
                    Log.d("cekError", error.message.toString())
                    when (error) {
                        is retrofit2.HttpException -> {
                            if (error.code() == 404) {
                                Log.d("cekHome", "error404")
                                hideUi()
                                emptyData()
                            } else if (error.code() == 500) {
                                Log.d("cekHome", "error500")
                                hideUi()
                                errorState()
                            }
                        }

                        is IOException -> {
                            Log.d("cekHome", "errorConnection")
                            hideUi()
                            noConnection()
                        }
                    }

                } else {
                    Log.d("cekHome", "errorUnknown")
                    showLoading(false)
                    hideUi()
                    errorState()
                }
            }
        }

    }

    private suspend fun sendAndGetData() {
        model.sendFilter().collectLatest {
            listMovies = it
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, listMovies)
        }
    }

    private fun hideError() {
        binding.gambarerror.visibility = GONE
        binding.errorTitle.visibility = GONE
        binding.errorDesc.visibility = GONE
        binding.resetButton.visibility = GONE
    }

    private fun hideUi() {
        binding.recyclerView.visibility = GONE
    }

    private fun showUi() {
        binding.recyclerView.visibility = VISIBLE
    }

    private fun emptyData() {
        binding.recyclerView.visibility = GONE
        binding.gambarerror.visibility = VISIBLE
        binding.errorTitle.visibility = VISIBLE
        binding.errorTitle.text = getString(R.string.errorTitle)
        binding.errorDesc.visibility = VISIBLE
        binding.errorDesc.text = getString(R.string.errorDesc)
        binding.resetButton.visibility = VISIBLE
        binding.resetButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sendAndGetData()
            }
        }
    }

    private fun noConnection() {
        binding.recyclerView.visibility = GONE
        binding.gambarerror.visibility = VISIBLE
        binding.errorTitle.visibility = VISIBLE
        binding.errorTitle.text = getString(R.string.errorTitleConnection)
        binding.errorDesc.visibility = VISIBLE
        binding.errorDesc.text = getString(R.string.errorDescConnection)
        binding.resetButton.visibility = VISIBLE
        binding.resetButton.text = getString(R.string.refreshButtonError)
        binding.resetButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sendAndGetData()
            }
        }
    }

    private fun errorState() {
        binding.recyclerView.visibility = GONE
        binding.gambarerror.visibility = VISIBLE
        binding.errorTitle.visibility = VISIBLE
        binding.errorTitle.text = getString(R.string.errorTitle500)
        binding.errorDesc.visibility = VISIBLE
        binding.errorDesc.text = getString(R.string.errorDesc500)
        binding.resetButton.visibility = VISIBLE
        binding.resetButton.text = getString(R.string.refreshButtonError)
        binding.resetButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                sendAndGetData()
            }
        }
    }

    private fun settingAdapter() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = pagingAdapter

        binding.recyclerView.adapter = pagingAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { pagingAdapter.retry() }
        )
        (binding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == pagingAdapter.itemCount) {
                        2
                    } else {
                        1
                    }
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = VISIBLE
        } else {
            binding.progressBar.visibility = GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}