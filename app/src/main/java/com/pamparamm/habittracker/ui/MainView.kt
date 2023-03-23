package com.pamparamm.habittracker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pamparamm.habittracker.domain.Habit
import com.pamparamm.habittracker.ui.pages.HabitEditorPageView
import com.pamparamm.habittracker.ui.navigation.TopBarView
import com.pamparamm.habittracker.ui.pages.AboutPageView
import com.pamparamm.habittracker.ui.navigation.SideDrawerView
import com.pamparamm.habittracker.ui.pages.HabitsPageView
import com.pamparamm.habittracker.ui.theme.Icons
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    startDestination: String = "habits",
) {
    fun toUUIDOrNull(uuidString: String?): UUID =
        if (uuidString == null || uuidString == "null") UUID.fromString("00000000-0000-0000-0000-000000000000") else UUID.fromString(
            uuidString
        )

    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val habits = remember { mutableStateListOf<Habit>() }
    val showHideScaffold: () -> Unit =
        { scope.launch { scaffoldState.drawerState.apply { if (isClosed) open() else close() } } }
    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState, topBar = {
        TopBarView(title = "Habit Tracker", buttonIcon = Icons.Menu, onClick = showHideScaffold)
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
                composable(route = "habits") { HabitsPageView(habits = habits, navController) }
                composable(route = "habits/{habitIndex}") { bse ->
                    HabitEditorPageView(
                        habits,
                        selectedIndex = toUUIDOrNull(bse.arguments?.getString("habitIndex")),
                        navController
                    )
                }
                composable(route = "about") { AboutPageView() }
            }
        }
    }
}