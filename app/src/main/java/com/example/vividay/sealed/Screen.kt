package com.example.vividay.sealed

import androidx.annotation.StringRes
import com.example.vividay.R

sealed class Screen(val route: String, @StringRes val resource: Int) {
    object Home : Screen("data", R.string.data)
    object Create : Screen("input", R.string.input)
    object Profile : Screen("profile", R.string.profile)
    object DataDetail : Screen("dataDetail", R.string.dataDetail)
}