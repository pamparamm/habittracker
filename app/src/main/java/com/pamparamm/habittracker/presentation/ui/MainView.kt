package com.pamparamm.habittracker.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.R
import com.pamparamm.habittracker.presentation.ui.pages.HabitsListPageView
import com.pamparamm.habittracker.presentation.ui.navigation.SideDrawerView
import com.pamparamm.habittracker.presentation.ui.navigation.TopBarView
import com.pamparamm.habittracker.presentation.ui.pages.HabitsEditorPageView
import com.pamparamm.habittracker.presentation.ui.pages.SettingsPageView
import com.pamparamm.habittracker.presentation.ui.theme.Icons
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.HabitsEditorViewModel
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListViewModel
import kotlinx.coroutines.launch

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    startDestination: String = NavRoutes.HABITS_LIST,
) {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    val showHideScaffold: () -> Unit =
        { scope.launch { scaffoldState.drawerState.apply { if (isClosed) open() else close() } } }

    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState, topBar = {
        TopBarView(
            title = stringResource(R.string.app_name),
            buttonIcon = Icons.Menu,
            onClick = showHideScaffold
        )
    }, drawerContent = {
        SideDrawerView(navController, showHideScaffold, Modifier.fillMaxWidth())
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavHost(
                modifier = modifier,
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(NavRoutes.HABITS_LIST) {
                    val vm = hiltViewModel<HabitsListViewModel>()
                    HabitsListPageView(vm, navController)
                }
                composable(
                    "${NavRoutes.HABITS_EDITOR}?${NavRoutes.HABITS_EDITOR_HABIT_UUID}={${NavRoutes.HABITS_EDITOR_HABIT_UUID}}",
                    arguments = listOf(navArgument(NavRoutes.HABITS_EDITOR_HABIT_UUID) {
                        nullable = true
                        defaultValue = null
                        type = NavType.StringType
                    })
                ) {
                    val vm = hiltViewModel<HabitsEditorViewModel>()
                    HabitsEditorPageView(vm, navController)
                }
                composable(NavRoutes.SETTINGS) { SettingsPageView() }
            }
        }
    }
}