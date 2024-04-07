package ui

import API.GetBooks
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import models.BookCategory
import models.BookContainer
import models.Books
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import wasmdemo.composeapp.generated.resources.Res
import wasmdemo.composeapp.generated.resources.book
import wasmdemo.composeapp.generated.resources.headphones
import wasmdemo.composeapp.generated.resources.menu_book

class BookUI {

    //ui of single book item
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun bookItem(product: Books, onItemClick: (Books) -> Unit) {

        // Detect click and invoke the onItemClick lambda
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // The book
                Card(
                    modifier = Modifier
                        .size(height = 250.dp, width = 200.dp)
                        .fillMaxSize(),
                ) {
                    AsyncImage(
                        model = product.coverThumbnailImage, // replace with working URL
                        placeholder = painterResource(Res.drawable.book),
                        error = painterResource(Res.drawable.book),
                        fallback = painterResource(Res.drawable.book),
                        contentDescription = "${product.title} available",
                        modifier = Modifier
                            .size(20.dp) // size of the image
                            .padding(30.dp),
                        contentScale = ContentScale.Crop // Crop the image if necessary to fit
                    )
//                    Image(
//                        painter = painterResource(Res.drawable.book),
//                        contentDescription = "${product.title} available",
//                        modifier = Modifier
//                            .size(20.dp) // size of the image
//                            .padding(30.dp),
//                        contentScale = ContentScale.Crop // Crop the image if necessary to fit
//                    )
                }
                // The text
                product.title?.let {
                    Text(
                        modifier = Modifier
                            .padding(top = 12.dp),
                        text = it,
                        fontWeight = FontWeight.Bold
                    )
                }

                //Text(text = product.authors, fontSize = 15.sp)
                Text(text = "hello", fontSize = 15.sp)
                Row {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Blue
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Blue
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Blue
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color.Blue
                    )
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Rating",
                        tint = Color.Blue
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(2.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp),
                        painter = painterResource(Res.drawable.headphones),
                        contentDescription = null
                    )
                    Image(
                        modifier = Modifier
                            .size(20.dp),
                        painter = painterResource(Res.drawable.menu_book),
                        contentDescription = null
                    )
                }
            }
        }
    }

    //ui of single book category item
    @Composable
    fun categoryItem(category: BookCategory) {
        val scope = rememberCoroutineScope()
        var bookContainer: BookContainer? = null
        scope.launch {
            //greeting = Greeting().greet()
            bookContainer = GetBooks().retrieveBooksFromAPI()
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(24.dp))
            Text(category.categoryDesc)
            Spacer(Modifier.height(12.dp))
            Text(category.categoryName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            LazyRow() {
                items(category.books) { product ->
                    product.coverThumbnailImage = bookContainer?.book?.coverThumbnailImage
                    bookItem(product = product, onItemClick = { selectedProduct ->
                        // Handle item click
                        // You can navigate to a detail screen, show a dialog, etc.
                    })
                }
            }
        }
        Column(
            Modifier.fillMaxWidth().padding(top = 4.dp, end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(category.viewAllText, color = Color(0xFF6200EE), textAlign = TextAlign.End)
        }
    }
}