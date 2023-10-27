package com.example.core.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.dataStore.LocalDatabase
import com.example.core.retrofit.ApiService
import com.example.core.retrofit.response.Movies
import kotlinx.coroutines.flow.first

class PagingSource(
    private val apiService: ApiService,
    private val localDb: LocalDatabase
) : PagingSource<Int, Movies>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val tokenUser = localDb.getAccessToken().first()
            Log.d("tokenForPaging", tokenUser)
            Log.d("page", page.toString())
            val responseData = apiService.getMovieList(
                "Bearer $tokenUser",
                page
            )

            LoadResult.Page(
                data = responseData.result,
                prevKey = null,
                nextKey = if (page == responseData.totalPages) null else page.plus(1)
            )
        } catch (exception: Exception) {
            Log.d("pagingError", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}