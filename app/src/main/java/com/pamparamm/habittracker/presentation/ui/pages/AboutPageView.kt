package com.pamparamm.habittracker.presentation.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pamparamm.habittracker.BuildConfig
import com.pamparamm.habittracker.R

@Composable
fun AboutPageView() {
    Column {
        Text(stringResource(R.string.app_name))
        Divider()
        Text(
            "${stringResource(R.string.about_version_name)} : ${BuildConfig.VERSION_NAME}" +
                    "\n${stringResource(R.string.about_version_code)} : ${BuildConfig.VERSION_CODE}"
        )
    }
}