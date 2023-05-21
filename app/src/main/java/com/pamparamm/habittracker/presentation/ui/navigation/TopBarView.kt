package com.pamparamm.habittracker.presentation.ui.navigation

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun TopBarView(
    title: String,
    buttonIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick) {
                Icon(buttonIcon, contentDescription = title)
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        modifier = modifier
    )
}