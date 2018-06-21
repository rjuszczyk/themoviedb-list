package com.example.radek.movielist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.example.radek.jobexecutor.InitialPagedResponse
import com.example.radek.jobexecutor.MainNetworkRepository
import com.example.radek.jobexecutor.PagedDataProvider
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
class MainViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    val fakeItem = NetResult(0, "", "", "", "")
    @Mock
    lateinit var pagedDataProvider: PagedDataProvider<NetResult>
    @Mock
    lateinit var movieListPagedDataProviderFactory: MovieListPagedDataProviderFactory
    @Mock
    lateinit var sortOptionsProvider : SortOptionsProvider
    lateinit var mainViewModel: MainViewModel
    lateinit var mainNetworkRepository: MainNetworkRepository<NetResult>

    @Before
    fun setup() {
        mainNetworkRepository = MainNetworkRepository(pagedDataProvider)
        Mockito.`when`(movieListPagedDataProviderFactory.create(any())).thenReturn(pagedDataProvider)
        val executor = Executor { p0 -> p0.run {  } }
        mainViewModel = MainViewModel(sortOptionsProvider, movieListPagedDataProviderFactory, mainNetworkRepository, executor)
    }

    @Test
    fun `view model observes repository`() {
        assertEquals(mainViewModel.repositoryState, mainNetworkRepository.repositoryState)
    }

    @Test
    fun `starts providing after is constructed`() {
        mainViewModel.list.observeForever({})
        verify(pagedDataProvider).provideInitialData(any(), any())
    }

    @Test
    fun `emits result when loaded`() {
        val observer = mock<Observer<PagedList<NetResult>>>()
        mainViewModel.list.observeForever(observer)
        val captor = argumentCaptor<(InitialPagedResponse<NetResult>) -> Unit>()
        verify(pagedDataProvider).provideInitialData(captor.capture(), any())

        val loadedList:List<NetResult> = listOf(fakeItem)
        val result = InitialPagedResponse(10,loadedList)

        captor.lastValue.invoke(result)

        Mockito.verify(observer).onChanged(any())
    }
}
