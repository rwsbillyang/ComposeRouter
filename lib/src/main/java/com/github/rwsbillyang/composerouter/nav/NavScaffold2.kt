package com.github.rwsbillyang.composerouter.nav


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerDefaults

import androidx.compose.material.Icon
import androidx.compose.material.IconButton

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.rwsbillyang.composerouter.Router
import com.github.rwsbillyang.composerouter.ScaffoldScreen2
import com.github.rwsbillyang.composerouter.ScaffoldScreen3


/**
 * Wrapper of Material2 Scaffold with TopAppBar as nav bar
 * */
@Composable
fun NavScaffold2(
    appNameResId: Int,
    modifier: Modifier = Modifier
)
{
    val screen = Router.currentRoute?.screen
    if (screen != null){
        if(screen is ScaffoldScreen2){
            Scaffold(
                modifier,screen.scaffoldState?: rememberScaffoldState(),
                screen.topBar?:{ NavTopAppBar(appNameResId, screen.topBarActions) },
                screen.bottomBar,
                screen.snackbarHost,
                screen.floatingActionButton,
                screen.floatingActionButtonPosition,
                screen.isFloatingActionButtonDocked,
                screen.drawerContent,
                screen.drawerGesturesEnabled,
                screen.drawerShape?: MaterialTheme.shapes.large,
                screen.drawerElevation,
                screen.drawerBackgroundColor?: MaterialTheme.colors.surface,
                screen.drawerContentColor?: contentColorFor(screen.drawerBackgroundColor?:MaterialTheme.colors.surface),
                screen.drawerScrimColor?: DrawerDefaults.scrimColor,
                screen.backgroundColor?:MaterialTheme.colors.background,
                screen.contentColor?:contentColorFor(screen.backgroundColor?:MaterialTheme.colors.background))
            {
                Router.Screen(it)
            }
        }else{
            if(screen is ScaffoldScreen3){
                Log.w(Router.TAG, "You are using ScaffoldScreen3 in NavScaffold2 of material2")
            }
            Scaffold(modifier, topBar =  { NavTopAppBar(appNameResId) {} })
            {
                Router.Screen(it)
            }
        }
    }else{

        Scaffold(modifier, topBar =  { NavTopAppBar(appNameResId) {} }){
            Row(Modifier.padding(it).fillMaxSize(), Arrangement.Center, Alignment.CenterVertically){
                Text("No route?")
            }
        }
    }
}

@Composable
fun NavTopAppBar(appNameResId: Int, actions:  @Composable RowScope.() -> Unit){
    val navIcon by remember(Router.currentRoute) {
        mutableStateOf(if(Router.isLast()) Icons.Filled.Close else Icons.Filled.ArrowBack)
    }

    val ctx =  LocalContext.current
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
        actions = actions
    )
}