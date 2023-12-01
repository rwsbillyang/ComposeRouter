package com.github.rwsbillyang.composerouter


import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


class ScaffoldScreen3(
    content: @Composable (call: ScreenCall) -> Unit,
    val topBarActions: @Composable RowScope.() -> Unit = {},
    val modifier: Modifier? = null,
    val topBar: (@Composable () -> Unit)? = null,
    val bottomBar: @Composable () -> Unit = {},
    val snackbarHost: @Composable () -> Unit = {},
    val floatingActionButton: @Composable () -> Unit = {},
    val floatingActionButtonPosition: FabPosition = FabPosition.End,
    val containerColor: Color? = null,
    val contentColor: Color? = null,
    val contentWindowInsets: WindowInsets? = null,
    val drawerContent: (@Composable ColumnScope.() -> Unit)? = null,
    val drawerModifier: Modifier? = null,
    val gesturesEnabled: Boolean = true,
    val scrimColor: Color? = null
) : Screen(content)