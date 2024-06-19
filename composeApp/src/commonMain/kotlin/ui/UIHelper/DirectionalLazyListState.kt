package ui.UIHelper

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import ui.ScrollDirection

class DirectionalLazyListState(
    private val lazyListState: LazyListState
) {
    private var positionY = lazyListState.firstVisibleItemScrollOffset
    private var visibleItem = lazyListState.firstVisibleItemIndex

    val scrollDirection by derivedStateOf {
        if (lazyListState.isScrollInProgress.not()) {
            ScrollDirection.None
        } else {
            val firstVisibleItemIndex = lazyListState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset =
                lazyListState.firstVisibleItemScrollOffset

            // We are scrolling while first visible item hasn't changed yet
            if (firstVisibleItemIndex == visibleItem) {
                val direction = if (firstVisibleItemScrollOffset > positionY) {
                    ScrollDirection.Down
                } else {
                    ScrollDirection.Up
                }
                positionY = firstVisibleItemScrollOffset

                direction
            } else {

                val direction = if (firstVisibleItemIndex > visibleItem) {
                    ScrollDirection.Down
                } else {
                    ScrollDirection.Up
                }
                positionY = firstVisibleItemScrollOffset
                visibleItem = firstVisibleItemIndex
                direction
            }
        }
    }
}
