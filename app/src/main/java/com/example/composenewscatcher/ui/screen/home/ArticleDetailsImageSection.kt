package com.example.composenewscatcher.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composenewscatcher.R
import com.example.composenewscatcher.ui.screen.SharedScreenState
import com.example.composenewscatcher.ui.util.GrayIndicatorBox

@Composable
fun ArticleDetailsImageSection(
    sharedScreenState: SharedScreenState,
    onReturnButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(sharedScreenState.requestedArticle?.media ?: painterResource(R.drawable.placeholder_no_image_available))
                .crossfade(true)
                .error(R.drawable.placeholder_no_image_available)
                .build(),
            contentDescription = "Article image",
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.TopCenter
        )

        TopBarActions(
            sharedScreenState = sharedScreenState,
            onReturnButtonClicked = { onReturnButtonClicked() },
            onSaveButtonClicked = { onSaveButtonClicked() }
        )
    }
}

@Composable
private fun TopBarActions(
    sharedScreenState: SharedScreenState,
    onReturnButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
) {
    var isDropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 22.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        GrayIndicatorBox(
            boxAlpha = 0.8f,
            modifier = Modifier.clickable {
                onReturnButtonClicked()
            }
        ) {
            Row(
                modifier = Modifier
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Return arrow icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    modifier = Modifier
                        .widthIn(0.dp, 100.dp),
                    text = sharedScreenState.requestedArticle?.title ?: stringResource(R.string.placeholder_text_no_title),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer
                        .copy(alpha = 0.9f)
                )
                .clickable {
                    isDropdownExpanded = !isDropdownExpanded
                }
        ) {
            Icon(
                modifier = Modifier
                    .padding(11.dp),
                painter = painterResource(R.drawable.ic_three_dots),
                contentDescription = "Three dots icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )

            MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(20.dp))) {
                DropdownMenu(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp)),
                    offset = DpOffset((-20).dp, 2.dp),
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }) {

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(30.dp),
                                imageVector = if (sharedScreenState.requestedArticle?.isSaved!!) {
                                    Icons.Filled.Bookmark
                                } else {
                                    Icons.Outlined.BookmarkBorder
                                },
                                contentDescription = "Saved icon",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        text = { DropdownItemText(stringResource(R.string.text_save)) },
                        onClick = {
                            onSaveButtonClicked()
                        })

                    DropdownMenuItem(
                        onClick = { },
                        text = { DropdownItemText(stringResource(R.string.text_share)) },
                        leadingIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(30.dp),
                                painter = painterResource(R.drawable.ic_share),
                                contentDescription = "Share icon",
                                tint = Color.Unspecified
                            )
                        })
                }
            }
        }
    }
}

@Composable
private fun DropdownItemText(text: String) {
    Text(
        modifier = Modifier.padding(start = 20.dp, end = 40.dp),
        text = text,
        fontSize = 17.sp,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Start
    )
}