package com.github.rwsbillyang.composerouter

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

/**
 * based on Scaffold, extend NavScreen by adding parameters of scaffold
 * */
class ScaffoldScreen(
    val content: @Composable (call: ScreenCall) -> Unit,
    val actions: @Composable RowScope.() -> Unit = {}
    // ... add Composable in Scaffold
)