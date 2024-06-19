package ui.screens

import Viewmodels.AllBooksViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import models.AllBooksData
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.BookUI
import wasmdemo.composeapp.generated.resources.Res
import wasmdemo.composeapp.generated.resources.left_arrow

class AllBooksScreen : Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel() { AllBooksViewModel() }
        val allBooks = rememberSaveable() { mutableStateOf<AllBooksData?>(null) }
        var showContent by remember { mutableStateOf(false) }
        LaunchedEffect(screenModel.allBooks) {
            screenModel.allBooks.collect { books ->
                // Update the UI here
                showContent = true
                // Cache the fetched data
//                    cachedBooks[currentIndex] = books
                if (books != null) {
                    allBooks.value = books.record
                }
            }
        }

        Column(
            Modifier.fillMaxWidth().padding(all = 16.dp).background(MaterialTheme.colors.background)
        ) {
            Button(
                modifier = Modifier.height(30.dp),
                onClick = { navigator.pop() },
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 15.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Image(
                    painterResource(Res.drawable.left_arrow),
                    contentDescription = "browse button icon",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
                Text(
                    text = " Back",
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
                )
            }
            if (showContent)
                BookUI().allBooksItem(allBooks.value)
        }
    }
}





