package com.example.radek.movielist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.example.radek.jobexecutor.response.InitialPagedResponse
import com.example.radek.jobexecutor.PageProviderExecutor
import com.example.radek.jobexecutor.PagedDataProvider
import com.example.radek.jobexecutor.State
import com.example.radek.model.MovieItem
import com.example.radek.model.provider.SortOptionsProvider
import com.example.radek.movielist.data.MovieListPagedDataProviderFactory
import com.example.radek.movielist.data.network.model.Movie
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner::class)
class MovieListViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    val fakeItem = Movie(0, "", "", "", "")
    @Mock
    lateinit var pagedDataProvider: PagedDataProvider<MovieItem>
    @Mock
    lateinit var movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory
    @Mock
    lateinit var sortOptionsProvider : SortOptionsProvider
    lateinit var movieListViewModel: MovieListViewModel
    lateinit var pageProviderExecutor: PageProviderExecutor<MovieItem>

    @Before
    fun setup() {
        pageProviderExecutor = PageProviderExecutor()
        Mockito.`when`(movieListPagedDataProviderFactory.create(any())).thenReturn(pagedDataProvider)
        val executor = Executor { p0 -> p0.run {  } }
        movieListViewModel = MovieListViewModel(sortOptionsProvider, movieListPagedDataProviderFactory, pageProviderExecutor, executor)
    }

    @Test
    fun `view model observes repository`() {
        val observer = mock<Observer<State>>()
        movieListViewModel.repositoryState.observeForever(observer)
        pageProviderExecutor.repositoryState(State.Loaded)
        verify(observer).onChanged(State.Loaded)
    }

    @Test
    fun `starts providing after is constructed`() {
        movieListViewModel.list.observeForever({})
        verify(pagedDataProvider).provideInitialData(any(), any())
    }

    @Test
    fun `emits result when loaded`() {
        val observer = mock<Observer<PagedList<MovieItem>>>()
        movieListViewModel.list.observeForever(observer)
        val captor = argumentCaptor<(InitialPagedResponse<MovieItem>) -> Unit>()
        verify(pagedDataProvider).provideInitialData(captor.capture(), any())

        val loadedList:List<Movie> = listOf(fakeItem)
        val result = InitialPagedResponse<MovieItem>(10, loadedList)

        captor.lastValue.invoke(result)

        Mockito.verify(observer).onChanged(any())
    }
}
