package com.pamparamm.habittracker.presentation.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.R

@Composable
fun SideDrawerView(
    navController: NavController,
    showHideScaffold: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(stringResource(R.string.menu_name), modifier = Modifier.padding(16.dp))
        Divider()
        Button(
            onClick = {
                navController.navigate(NavRoutes.HABITS_LIST)
                showHideScaffold()
            },
            modifier
        ) {
            Text(text = stringResource(R.string.habits_list_name))
        }
        Button(
            onClick = {
                navController.navigate(NavRoutes.SETTINGS)
                showHideScaffold()
            },
            modifier
        ) {
            Text(text = stringResource(R.string.settings_name))
        }
    }
}