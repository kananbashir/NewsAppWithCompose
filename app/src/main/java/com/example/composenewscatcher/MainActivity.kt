package com.example.composenewscatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.composenewscatcher.ui.navigation.SetupNavGraph
import com.example.composenewscatcher.ui.theme.ComposeNewsCatcherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNewsCatcherTheme {
                val navController = rememberNavController()

                SetupNavGraph(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}