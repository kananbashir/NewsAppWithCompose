package com.example.composenewscatcher.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composenewscatcher.R
import com.example.composenewscatcher.data.model.Article
import com.example.composenewscatcher.util.extension.firstLetterCapitalize

@Composable
fun LazyColumnArticleItem(
    article: Article,
    modifier: Modifier = Modifier,
    onArticleItemClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp)
            .clickable { onArticleItemClicked() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyColumnArticleItemText(
            modifier = Modifier.weight(1f),
            article = article
        )

        Spacer(modifier = Modifier.width(15.dp))

        AsyncImage(
            modifier = Modifier
                .size(130.dp, 100.dp)
                .clip(RoundedCornerShape(20.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.media ?: painterResource(R.drawable.placeholder_no_image_available))
                .crossfade(true)
                .error(R.drawable.placeholder_no_image_available)
                .build(),
            contentDescription = "Article item",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun LazyColumnArticleItemText(
    article: Article,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        GrayIndicatorBox {
            Text(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                    ),
                text = article.topic?.firstLetterCapitalize() ?: stringResource(R.string.placeholder_text_no_topic),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = article.title ?: stringResource(R.string.placeholder_text_no_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = article.publishedDate ?: stringResource(R.string.placeholder_text_no_publish_date),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.width(15.dp))

            Text(
                text = article.author ?: stringResource(R.string.placeholder_text_no_author),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}