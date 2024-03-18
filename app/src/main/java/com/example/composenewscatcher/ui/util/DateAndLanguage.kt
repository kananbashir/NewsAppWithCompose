package com.example.composenewscatcher.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenewscatcher.ui.screen.SharedScreenState
import com.example.composenewscatcher.ui.theme.RedwoodRed
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateAndLanguageSection(
    modifier: Modifier = Modifier,
    withLanguageSelector: Boolean,
    sharedScreenState: SharedScreenState,
    onLanguageSelectionButtonClicked: (() -> Unit)? = null
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrentDate()
        if (withLanguageSelector) {
            onLanguageSelectionButtonClicked?.let {
                LanguageSettingButton(
                    sharedScreenState = sharedScreenState,
                    onLanguageSelectionButtonClicked = { it() }
                )
            }
        }
    }
}

@Composable
private fun CurrentDate() {
    Text(
        text = LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd MMMM yyyy")
        ),
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
private fun LanguageSettingButton(
    modifier: Modifier = Modifier,
    sharedScreenState: SharedScreenState,
    onLanguageSelectionButtonClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 5.dp
            )
            .clickable {
                onLanguageSelectionButtonClicked()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sharedScreenState.appLanguage?.uppercase() ?: "ENN",
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(RedwoodRed)
            )
        }
    }
}
