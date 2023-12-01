package com.github.rwsbillyang.composerouter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.material.ScaffoldState

open class Screen(val content: @Composable (call: ScreenCall) -> Unit)

/**
 * parameters comes from route will passs to  scaffold in NavScaffold2
 * */
class ScaffoldScreen2(
    content: @Composable (call: ScreenCall) -> Unit,
    val topBarActions: @Composable RowScope.() -> Unit = {},
    val topBar: (@Composable () -> Unit)? = null,
    val scaffoldState: ScaffoldState? = null,
    val bottomBar: @Composable () -> Unit = {},
    val snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
    val floatingActionButton: @Composable () -> Unit = {},
    val floatingActionButtonPosition: FabPosition = FabPosition.End,
    val isFloatingActionButtonDocked: Boolean = false,
    val backgroundColor: Color? = null,
    val contentColor: Color? = null,
    val drawerContent: @Composable (ColumnScope.() -> Unit)? = null,
    val drawerGesturesEnabled: Boolean = true,
    val drawerShape: Shape? = null,
    val drawerElevation: Dp = DrawerDefaults.Elevation,
    val drawerBackgroundColor: Color? = null,
    val drawerContentColor: Color? = null,
    val drawerScrimColor: Color? = null
): Screen(content)