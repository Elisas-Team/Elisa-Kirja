package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import models.AllBooksData
import models.BookCategory
import models.BookContainer
import models.Books
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.UIHelper.DirectionalLazyListState
import ui.UIHelper.LoadingEffect
import ui.screens.AllBooksScreen
import wasmdemo.composeapp.generated.resources.Res
import wasmdemo.composeapp.generated.resources.arrow_right
import wasmdemo.composeapp.generated.resources.book
import wasmdemo.composeapp.generated.resources.headphones
import wasmdemo.composeapp.generated.resources.menu_book

class BookUI {
    @Composable
    fun rememberDirectionalLazyListState(
        lazyListState: LazyListState,
    ): DirectionalLazyListState {
        return remember {
            DirectionalLazyListState(lazyListState)
        }
    }

    // UI for a single book item
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun bookItem(product: Books, onItemClick: (Books) -> Unit) {
        val imageUrl = "https://api.codetabs.com/v1/proxy/?quest=" + product.coverThumbnailImage

        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally() + expandIn(
                // Expand from the top.
                expandFrom = Alignment.CenterStart
            ) + fadeIn(
                initialAlpha = 0.9f,
                animationSpec = tween(durationMillis = 20000),
            ),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.fillMaxWidth().background(MaterialTheme.colors.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .size(height = 250.dp, width = 200.dp)
                            .background(MaterialTheme.colors.background),
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .crossfade(800)
                                .build(),
                            placeholder = painterResource(Res.drawable.book),
                            error = painterResource(Res.drawable.book),
                            fallback = painterResource(Res.drawable.book),
                            contentDescription = "${product.title} available",
                            modifier = Modifier
                                .fillMaxSize().background(MaterialTheme.colors.background),
                            contentScale = ContentScale.Crop
                        )
                    }
                    product.title?.let {
                        Text(
                            modifier = Modifier.padding(top = 12.dp)
                                .background(MaterialTheme.colors.background),
                            text = it,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Text(text = "hello", style = MaterialTheme.typography.body1)
                    Row {
                        repeat(4) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Rating",
                                tint = MaterialTheme.colors.secondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colors.secondary
                        )
                    }
                    Row(
                        modifier = Modifier.padding(2.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp)
                                .background(MaterialTheme.colors.background),
                            painter = painterResource(Res.drawable.headphones),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                        )
                        Image(
                            modifier = Modifier.size(20.dp)
                                .background(MaterialTheme.colors.background),
                            painter = painterResource(Res.drawable.menu_book),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                        )
                    }
                }
            }
        }
    }

    // UI for a single book category item
    @Composable
    fun categoryItem(category: BookCategory, allBooks: List<BookContainer?>) {
        val navigator = LocalNavigator.currentOrThrow
        var showContent by rememberSaveable() { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(200)
            showContent = true
        }
        Column(
            Modifier.fillMaxWidth().background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp).background(MaterialTheme.colors.background))
            androidx.compose.animation.AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                        delayMillis = 200
                    )
                )
            ) {
                Text(category.categoryDesc, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp).background(MaterialTheme.colors.background))
            androidx.compose.animation.AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearOutSlowInEasing,
                        delayMillis = 200
                    )
                )
            ) {
                Text(category.categoryName)
            }
            Spacer(Modifier.height(16.dp).background(MaterialTheme.colors.background))
            androidx.compose.animation.AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing,
                        delayMillis = 300
                    )
                ),
            ) {
                LazyRow {
                    itemsIndexed(allBooks) { index, bookContainer ->
                        val product = category.books[index]
                        product.coverThumbnailImage = bookContainer?.book?.coverThumbnailImage
                        bookItem(
                            product = product,
                            onItemClick = {
                                // Handle item click
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(
            Modifier.fillMaxWidth().padding(top = 4.dp, end = 16.dp)
                .background(MaterialTheme.colors.background),
            horizontalAlignment = Alignment.End
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearOutSlowInEasing,
                        delayMillis = 300
                    )
                ),
            ) {
                returnButton(category.viewAllText, 35.dp, navigator)
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    @Composable
    fun allBooksItem(books: AllBooksData?) {
        if (books != null) {
            if (books.books.isNotEmpty()) {
                val list = books.books
                list.let { books ->
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 250.dp),
                        content = {
                            items(books.size) { item ->
                                bookItem(books[item], onItemClick = {
                                    // handle item click
                                })
                            }
                        }
                    )
                }
            }
        } else {
            Column(
                Modifier.fillMaxWidth().fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingEffect().LoadingAnimation()
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun returnButton(text: String, size: Dp, navigator: Navigator) {
        return Button(
            modifier = Modifier.height(size),
            onClick = { navigator.push(AllBooksScreen()) },
            elevation = ButtonDefaults.elevation(
                defaultElevation = 10.dp,
                pressedElevation = 15.dp,
                disabledElevation = 0.dp
            ),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text(
                text,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold
            )
            Image(
                painterResource(Res.drawable.arrow_right),
                contentDescription = "browse button icon",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
        }
    }
}

