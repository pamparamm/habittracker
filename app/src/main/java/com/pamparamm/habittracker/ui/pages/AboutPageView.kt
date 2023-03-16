package com.pamparamm.habittracker.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pamparamm.habittracker.BuildConfig

@Composable
fun AboutPageView(modifier: Modifier = Modifier){
    Column(modifier) {
        Text("Habit Tracker Application")
        Divider()
        Text("Version Name : " + BuildConfig.VERSION_NAME + "\n" + "Version Code : " + BuildConfig.VERSION_CODE.toString())
    }
}