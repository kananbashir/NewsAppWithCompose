package com.example.composenewscatcher.ui.screen.language

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenewscatcher.R
import com.example.composenewscatcher.ui.util.GrayIndicatorBox

@Composable
fun LanguageSelectionScreen(
    modifier: Modifier = Modifier,
    onLanguageSelected: (selectedLanguage: String) -> Unit,
    onReturnButtonClicked: () -> Unit
) {
    Column(modifier = modifier) {
        GrayIndicatorBox(
            modifier = Modifier
                .padding(20.dp)
                .clickable {
                    onReturnButtonClicked()
                }
        ) {
            Icon(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        end = 45.dp,
                        top = 7.dp,
                        bottom = 7.dp
                    )
                    .size(23.dp),
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = "Arrow icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            val languages = Languages.values()

            items(languages) { language ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLanguageSelected(language.language)
                        },
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        modifier = Modifier
                            .padding(start = 30.dp),
                        text = language.language.uppercase(),
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onPrimary)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageSelectionScreenPrev() {
    LanguageSelectionScreen(
        modifier = Modifier
            .fillMaxSize(),
        onLanguageSelected = {},
        onReturnButtonClicked = {}
    )
}