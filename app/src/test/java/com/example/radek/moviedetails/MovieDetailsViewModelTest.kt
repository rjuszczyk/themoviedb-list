package com.example.radek.moviedetails

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.example.radek.model.MovieDetailsItem
import com.example.radek.model.provider.MovieDetailsProvider
import com.example.radek.moviedetail.MovieDetailsViewModel
import com.example.radek.moviedetail.State
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailsViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @Mock
    lateinit var movieDetailsProvider: MovieDetailsProvider
    @Mock
    lateinit var stateObserver: Observer<State>
    @Mock
    lateinit var movieDetailsObserver: Observer<MovieDetailsItem>
    lateinit var movieDetailsViewModel: MovieDetailsViewModel
    val itemId = 1
    @Before
    fun setup() {
        movieDetailsViewModel = MovieDetailsViewModel(movieDetailsProvider, itemId)
        movieDetailsViewModel.state.observeForever(stateObserver)
        movieDetailsViewModel.movieDetailsItem.observeForever(movieDetailsObserver)
    }

    @Test
    fun `provides data when created`() {
        verify(movieDetailsProvider).provideMovieDetails(eq(itemId), any())
    }

    @Test
    fun `shows loading when providing data`() {
        verify(stateObserver).onChanged(State.Loading)
    }
    @Test
    fun `shows loaded when providing data loaded`() {
        `when`(movieDetailsProvider.provideMovieDetails(eq(itemId), any())).thenAnswer {
            (it.arguments[1] as MovieDetailsProvider.Callback).onSuccess(mock())
        }
        verify(stateObserver).onChanged(State.Loaded)
    }

    @Test
    fun `shows error when providing data failed`() {
        val callbackCaptor = argumentCaptor<MovieDetailsProvider.Callback>()
        verify(movieDetailsProvider).provideMovieDetails(eq(itemId), callbackCaptor.capture())

        reset(stateObserver)
        callbackCaptor.lastValue.onFailed(Throwable())
        verify(stateObserver).onChanged(any<State.Failed>())
    }

    @Test
    fun `emits data when loaded successfully`() {
        val result = mock<MovieDetailsItem>()
        val callbackCaptor = argumentCaptor<MovieDetailsProvider.Callback>()
        verify(movieDetailsProvider).provideMovieDetails(eq(itemId), callbackCaptor.capture())
        callbackCaptor.lastValue.onSuccess(result)
        verify(movieDetailsObserver).onChanged(result)
    }

    @Test
    fun `loading when retry button clicked`() {
        reset(stateObserver)
        movieDetailsViewModel.retryLoading()
        verify(stateObserver).onChanged(State.Loading)
    }
}
