package ui.screens

import Greeting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.BookUI
import ui.LocalCustomColors
import wasmdemo.composeapp.generated.resources.Res
import wasmdemo.composeapp.generated.resources.arrow_right
import wasmdemo.composeapp.generated.resources.audiobook

@OptIn(ExperimentalResourceApi::class)
class HomeScreen : Screen {
    @Composable
    override fun Content() {
        var showContent by remember { mutableStateOf(false) }
        val customColors = LocalCustomColors.current
        val navigator = LocalNavigator.currentOrThrow
        var isLoading by remember { mutableStateOf(true) }

        // Simulate loading delay
        LaunchedEffect(Unit) {
            showContent = true
            isLoading = false
        }
        //main ui of the page
        LazyColumn(Modifier.fillMaxHeight().background(MaterialTheme.colors.background)) {
            item {
                Column(Modifier.fillMaxHeight().aspectRatio(2f), horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedVisibility(visible = showContent) {
                        val greeting = remember { Greeting().greet() }
                        Row(
                            Modifier.fillMaxSize()
                                .background(color = customColors.secondaryBackground)
                                .padding(horizontal = 42.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                Modifier.weight(0.6F, fill = true).padding(42.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    "$greeting",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 54.sp,
                                    color = MaterialTheme.colors.primary,
                                    lineHeight = 40.sp,
                                )
                                Spacer(Modifier.height(24.dp))
                                Text(
                                    Greeting().elisaDescription(),
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colors.secondary,
                                    fontWeight = FontWeight.W500,
                                    lineHeight = 30.sp,
                                )
                                Spacer(Modifier.height(24.dp))
                                Button(
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
                                        Greeting().browseBooks(),
                                        Modifier.padding(end = 10.dp),
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
                            Column(
                                Modifier.weight(0.4F, fill = true),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(painterResource(Res.drawable.audiobook), null)
                            }
                        }
                    }
                }
            }
            if (showContent && !isLoading) {
                items(Greeting().getBookCategories()) { category ->
                    BookUI().categoryItem(category)
                }
            }
        }
    }
}