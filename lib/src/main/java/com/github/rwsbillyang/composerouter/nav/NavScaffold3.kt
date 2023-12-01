package com.github.rwsbillyang.composerouter.nav


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.rwsbillyang.composerouter.Router
import com.github.rwsbillyang.composerouter.ScaffoldScreen2
import com.github.rwsbillyang.composerouter.ScaffoldScreen3
import kotlinx.coroutines.launch


/**
 * Wrapper of Material3 Scaffold with TopAppBar as nav bar
 * https://developer.android.google.cn/jetpack/compose/designsystems/material2-material3#m3_6
 * */
@Composable
fun NavScaffold3(
    appNameResId: Int,
    modifier: Modifier = Modifier
)
{
    val screen = Router.currentRoute?.screen
    if (screen != null){
        if(screen is ScaffoldScreen3){
            if(screen.drawerContent == null){
                Scaffold(
                    screen.modifier?: modifier,
                    screen.topBar?:{ NavTopAppBar3(appNameResId, screen.topBarActions) },
                    screen.bottomBar,
                    screen.snackbarHost,
                    screen.floatingActionButton,
                    screen.floatingActionButtonPosition,
                    screen.containerColor?: MaterialTheme.colorScheme.background,
                    screen.contentColor?: contentColorFor(containerColor),
                    screen.contentWindowInsets?: ScaffoldDefaults.contentWindowInsets)
                {
                    Router.Screen(it)
                }
            }else{
                ResponsiveDrawer({
                    Scaffold(
                        screen.modifier ?: modifier,
                        screen.topBar?: { NavTopAppBar3(appNameResId, screen.topBarActions) },
                        screen.bottomBar,
                        screen.snackbarHost,
                        screen.floatingActionButton,
                        screen.floatingActionButtonPosition,
                        screen.containerColor ?: MaterialTheme.colorScheme.background,
                        screen.contentColor ?: contentColorFor(containerColor),
                        screen.contentWindowInsets ?: ScaffoldDefaults.contentWindowInsets
                    ){
                        Router.Screen(it)
                    }
                },
                    screen.drawerModifier?: Modifier,
                    screen.gesturesEnabled,
                    screen.scrimColor?: DrawerDefaults.scrimColor,
                    screen.drawerContent
                )
            }
        }else{
            if(screen is ScaffoldScreen2){
                Log.w(Router.TAG, "You are using ScaffoldScreen2 in NavScaffold3 of material3")
            }
            Scaffold(modifier, topBar =  { NavTopAppBar3(appNameResId) {} })
            {
                Router.Screen(it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopAppBar3(appNameResId: Int, actions:  @Composable RowScope.() -> Unit){
    val navIcon by remember(Router.currentRoute) {
        mutableStateOf(if(Router.isLast()) Icons.Filled.Close else Icons.AutoMirrored.Filled.ArrowBack)
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ResponsiveDrawer(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    drawerContent: @Composable ColumnScope.() -> Unit
){
    val widthSizeClass = calculateWindowSizeClass(LocalContext.current as Activity).widthSizeClass
    DrawerHub(widthSizeClass, content, modifier, gesturesEnabled, scrimColor, drawerContent)
}

@Composable
fun DrawerHub(
    widthSizeClass: WindowWidthSizeClass,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    drawerContent: @Composable ColumnScope.() -> Unit
){
    val sheetWidth = when(widthSizeClass){
        WindowWidthSizeClass.Compact -> 200.dp
        WindowWidthSizeClass.Medium -> 300.dp
        else -> 400.dp
    }

    if (widthSizeClass == WindowWidthSizeClass.Compact) {
        Log.d("Router", "use ModalNavigationDrawer")
        ModalNavigationDrawer(
            {
                ModalDrawerSheet(
                    modifier = modifier.width(sheetWidth),
                    drawerShape =  RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                    content = drawerContent
                )
            },
            Modifier,
            rememberDrawerState(DrawerValue.Closed),
            gesturesEnabled,
            scrimColor, content)
    } else {
        Log.d("Router", "use PermanentNavigationDrawer")
        PermanentNavigationDrawer({
            ModalDrawerSheet(
                modifier = modifier.width(sheetWidth),
                drawerShape =  RectangleShape,
                content = drawerContent
            )
        },Modifier, content)
    }
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NavDrawerContent(
    items: List<String>,
    icons: List<ImageVector>?,
    onItemClick: (index: Int) -> Unit,
    content: @Composable () -> Unit
) {
    val widthSizeClass = calculateWindowSizeClass(LocalContext.current as Activity).widthSizeClass
    val selectedState = rememberSaveable { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    DrawerHub(widthSizeClass, content){
        items.forEachIndexed { index, item ->
            NavigationDrawerItem(
                label = { Text(item) },
                selected = index == selectedState.intValue,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedState.intValue = index
                    onItemClick(index)
                },
                modifier = Modifier.padding(horizontal = 12.dp),
                icon = { icons?.let{ Icon(icons[index], contentDescription = item) } },
            )
        }
    }
}