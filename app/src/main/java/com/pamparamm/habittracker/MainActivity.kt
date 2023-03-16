package com.pamparamm.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pamparamm.habittracker.ui.MainView
import com.pamparamm.habittracker.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackerTheme {
                MainView()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HabitTrackerTheme {
        MainView()
    }
}