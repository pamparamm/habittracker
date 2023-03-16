package com.pamparamm.habittracker.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SideDrawerView(
    navController: NavController,
    showHideScaffold: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text("Menu", modifier = Modifier.padding(16.dp))
        Divider()
        Button(
            onClick = {
                navController.navigate("habits")
                showHideScaffold()
            },
            modifier
        ) {
            Text(text = "Habits")
        }
        Button(
            onClick = {
                navController.navigate("about")
                showHideScaffold()
            },
            modifier
        ) {
            Text(text = "About")
        }
    }
}