package com.github.rwsbillyang.composerouter.ui


import android.app.Activity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.rwsbillyang.composerouter.Router


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleNavLayout(
    appNameResId: Int,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets
)
{
    val navIcon by remember(Router.currentRoute) {
        mutableStateOf(if(Router.isLast()) Icons.Filled.Close else Icons.Filled.ArrowBack)
    }

    val ctx =  LocalContext.current
    Scaffold(modifier,  {
        TopAppBar(
            title = {
                Text(
                    stringResource(Router.currentRoute?.titleId?: appNameResId),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    if(Router.isLast()) {
                        (ctx as Activity).finish()
                    } else Router.back()
                }) {
                    Icon(navIcon, contentDescription = "back")
                }
            } ,
            actions = Router.currentRoute?.component?.actions?:{}
        )
    },bottomBar, snackbarHost, floatingActionButton, floatingActionButtonPosition, containerColor, contentColor, contentWindowInsets){ padding ->
        Router.Screen(padding)
    }
}