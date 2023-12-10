package com.example.vividay.presentation.data_screen

import java.io.Serializable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

data class DropDownItem(
    val text: String,
    val color: String
): Serializable


@Composable
fun DayColorItem(
    dayColorName: String,
    dayColorItems: List<DropDownItem>,
    modifier: Modifier = Modifier,
    onItemClick: (DropDownItem) -> Unit
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Card(
        // elevation = 4.dp,
        modifier = modifier
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            println("Long press detected")
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        }
                    )
                }
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color = runCatching {
                        Color(
                            android.graphics.Color.parseColor(
                                dayColorItems.firstOrNull()?.color ?: ""
                            )
                        )
                    }.getOrElse { Color.Transparent })
            )
            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (offset != Offset.Zero) {
                            isContextMenuVisible = true
                        }
                    }
                }
            ) {
                dayColorItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(color = runCatching {
                                            Color(android.graphics.Color.parseColor(item.color))
                                        }.getOrElse { Color.Transparent })
                                )
                                Text(text = item.text)
                            }
                        },
                        onClick = {
                            onItemClick(item)
                            isContextMenuVisible = false
                        }
                    )
                }
            }
            Text(text = dayColorName)
        }
    }
}

