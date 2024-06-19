package Viewmodels

import API.BooksService
import Strings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import models.BookContainer

//this viewmodel class is inheriting the screenmodel class of voyager library and is similar to the android viewmodel
class BooksViewModel() : ScreenModel {
    private val allBooksStateFlow = MutableStateFlow<List<BookContainer?>>(emptyList())
    val allBooks: StateFlow<List<BookContainer?>> get() = allBooksStateFlow
    private val _bookStateFlow = MutableStateFlow<BookContainer?>(null)
    private val _newBooksStateFlow = MutableStateFlow<Boolean?>(false)
    private val cachedBooks = mutableMapOf<Int, List<BookContainer?>>()
    val isNewBooksAdded: StateFlow<Boolean?> get() = _newBooksStateFlow
    private val apiService = BooksService()
    private var categories = Strings().getBookCategories()
    private val _endReachedFlow = MutableStateFlow<Boolean?>(false)
    val hasReachedEnd: StateFlow<Boolean?> get() = _endReachedFlow

    init {
        fetchMoreData(0)
    }

    private fun fetchMoreData(currentIndex: Int) {
        var count = 0;
        if (currentIndex < categories.size) {
            val category = categories[currentIndex]
            category.books.forEach {
                it.id?.let { it1 -> fetchData(it1, currentIndex) }
                count++
            }
            if ((currentIndex > 0 && currentIndex <= Strings().getBookCategories().size) && count == category.books.size)
                _newBooksStateFlow.update { true }
        }
        if (currentIndex == categories.size) {
            _endReachedFlow.update { true }
        }
    }

    private fun fetchData(bookId: String, currentIndex: Int) {
        screenModelScope.launch {
            val response = apiService.retrieveBooksFromAPI(bookId)
            val fetchedBooks = response?.book
            val bookContainers = listOf(fetchedBooks?.let { BookContainer(it) })
            cachedBooks[currentIndex] = bookContainers
            bookContainers.forEach { user ->
                allBooksStateFlow.update { it + user }
            }
        }
    }

    fun isDataLoaded(): Boolean {
        return allBooks.value.isNotEmpty() && allBooks.value.size == Strings().getBooks().size
    }

    fun loadMoreData(currentIndex: Int) {
        println("mango load more called, $currentIndex")
        if (cachedBooks.containsKey(currentIndex)) {
            // Return cached data
            allBooksStateFlow.update { it + cachedBooks[currentIndex]!! }
        } else {
            fetchMoreData(currentIndex)
        }
    }
}