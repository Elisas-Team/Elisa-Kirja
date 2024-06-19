package ui.screens

import Strings
import Viewmodels.BooksViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import models.BookContainer
import ui.BookUI
import ui.LocalCustomColors
import ui.ScrollDirection
import ui.UIHelper.InfiniteListHandler

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel() { BooksViewModel() }
        var showContent by rememberSaveable() { mutableStateOf(false) }
        var moreContent by remember() { mutableStateOf(false) }
        var currentIndex by remember() { mutableStateOf(1) }
        val allBooks = rememberSaveable() { mutableStateOf<List<BookContainer?>>(emptyList()) }
        // a cachedBooks state to store the fetched data
        val cachedBooks by rememberSaveable {
            mutableStateOf<MutableMap<Int, List<BookContainer?>>>(
                mutableMapOf()
            )
        }
        val customColors = LocalCustomColors.current
        val listState = rememberLazyListState()
        val directionalLazyListState = BookUI().rememberDirectionalLazyListState(
            listState
        )

        // Observe the allBooks state flow and update the UI when data is loaded
        LaunchedEffect(screenModel.allBooks) {
            screenModel.allBooks.collect { books ->
                if (screenModel.isDataLoaded()) {
                    // Update the UI here
                    showContent = true
                    // Cache the fetched data
                    cachedBooks[currentIndex] = books
                    allBooks.value = books
                }
            }
        }
        LaunchedEffect(screenModel.isNewBooksAdded) {
            screenModel.isNewBooksAdded.collect { isNew ->
                if (isNew != null) {
                    moreContent = isNew
                }
            }
        }

        // Main UI of the page
        LazyColumn(
            state = listState,
            userScrollEnabled = showContent //to only enable once firs API collection fetched
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .background(customColors.secondaryBackground),
                ) {
                    BookUI().heroImage(showContent, listState)
                }
            }
            if (showContent && currentIndex <= Strings().getBookCategories().size) {
                items(currentIndex) { category ->
                    // Check if the data is already cached
                    val cachedCategoryBooks = cachedBooks[currentIndex]
                    if (cachedCategoryBooks != null) {
                        // Use the cached data
                        BookUI().categoryItem(
                            Strings().getBookCategories()[currentIndex],
                            cachedCategoryBooks
                        )
                    }

                    if (directionalLazyListState.scrollDirection == ScrollDirection.Down) {
                        InfiniteListHandler(listState, currentIndex, onLoadMore = {
                            if ((it + 1) <= Strings().getBookCategories().size) {
                                currentIndex = it + 1
                                screenModel.loadMoreData(it + 1)
                            }
                        })
                    }
                }
            }

            if (moreContent) {
                if (currentIndex <= Strings().getBookCategories().size) {
                    items(currentIndex) { category ->
                        BookUI().categoryItem(
                            Strings().getBookCategories()[category],
                            allBooks.value
                        )
                    }
                }
            }
        }
    }
}


