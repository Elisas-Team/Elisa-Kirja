package Viewmodels

import API.BooksService

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.AllBooksContainer

//this viewmodel class is inheriting the screenmodel class of voyager library and is similar to the android viewmodel
class AllBooksViewModel() : ScreenModel {
    private val allBooksStateFlow = MutableStateFlow<AllBooksContainer?>(null)
    val allBooks: StateFlow<AllBooksContainer?> get() = allBooksStateFlow
    private val _newBooksStateFlow = MutableStateFlow<Boolean?>(false)
    val isNewBooksAdded: StateFlow<Boolean?> get() = _newBooksStateFlow
    private val apiService = BooksService()
    private var currentPage = 1
    private val limit = 5
    private val _endReachedFlow = MutableStateFlow<Boolean?>(false)
    val hasReachedEnd: StateFlow<Boolean?> get() = _endReachedFlow

    init {
        fetchMoreData(currentPage)
    }

    private fun fetchMoreData(page: Int) {
        if (page <= limit) {
            fetchData(page)
        }

        if (page in 1..limit)
            _newBooksStateFlow.update { true }

        if (page == limit) {
            _endReachedFlow.update { true }
        }
    }

    private fun fetchData(page: Int) {
        screenModelScope.launch {
            val response = apiService.retrieveAllBooksOfCategory("483")
            val fetchedBooks = response?.record
            val allBooksContainers = fetchedBooks?.let { (AllBooksContainer(it)) }
            allBooksStateFlow.update { allBooksContainers }
        }
    }

    fun loadMoreData() {
        if (!hasReachedEnd.value!!) {
            currentPage++
            fetchMoreData(currentPage)
        }
    }
}