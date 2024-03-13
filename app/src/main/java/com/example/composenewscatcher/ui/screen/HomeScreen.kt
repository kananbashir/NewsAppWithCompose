package com.example.composenewscatcher.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composenewscatcher.R
import com.example.composenewscatcher.ui.theme.Inter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Header()
        Spacer(modifier = Modifier.height(30.dp))
        CurrentDate()
        Spacer(modifier = Modifier.height(30.dp))
        LazyRow {

        }
    }
}

@Composable
private fun Header() {
    Row (
        modifier = Modifier
            .padding(top = 25.dp, start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = stringResource(R.string.header_home),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun CurrentDate() {
    Text(
        modifier = Modifier
            .padding(start = 20.dp),
        text = LocalDate.now().format(
            DateTimeFormatter.ofPattern("dd MMMM yyyy")
        ),
        fontSize = 15.sp,
        color = LightGray,
        fontWeight = FontWeight.SemiBold
    )
}

@Preview (showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}