package com.github.rwsbillyang.composerouter.nav


import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
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
import com.github.rwsbillyang.composerouter.useRouter
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
    val router = useRouter()

    val screen = router.currentRoute?.screen
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
                    router.Screen(it)
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
                        router.Screen(it)
                    }
                },
                    screen.drawerModifier?: Modifier,
                    screen.gesturesEnabled,
                    screen.scrimColor?: DrawerDefaults.scrimColor,
                    rememberDrawerState(DrawerValue.Closed),
                    screen.drawerContent
                )
            }
        }else if(screen is ScaffoldScreen2){
            Log.w(Router.TAG, "You are using ScaffoldScreen2 in NavScaffold3 of material3")
            Scaffold(modifier, topBar =  { NavTopAppBar3(appNameResId) {} })
            {
                router.Screen(it)
            }
        }else {
            if(router.currentRoute!!.useNavScaffold){
                Scaffold(modifier, topBar =  { NavTopAppBar3(appNameResId) {} })
                {
                    router.Screen(it)
                }
            }else{//某些特殊情况下，如带drawer的scaffold，自行完全定制，更方便
                router.Screen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTopAppBar3(appNameResId: Int, actions:  @Composable RowScope.() -> Unit){
    val ctx = LocalContext.current
    val router = useRouter()

    val navIcon by remember(router.currentRoute) {
        mutableStateOf(if(router.isLast()) Icons.Filled.Close else Icons.AutoMirrored.Filled.ArrowBack)
    }

    TopAppBar(
        title = {
            val currentRoute = router.currentRoute
            Text((currentRoute?.ctxMap?.get("title") as String?)
                    ?: currentRoute?.titleId?.let { stringResource(it) } ?: currentRoute?.name
                    ?: stringResource(appNameResId),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if(router.isLast()) {
                    (ctx as Activity).finish()
                } else router.back()
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
    drawerState:DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawerContent: @Composable ColumnScope.() -> Unit
){
    val widthSizeClass = calculateWindowSizeClass(LocalContext.current as Activity).widthSizeClass
    DrawerHub(widthSizeClass, content, modifier, gesturesEnabled, scrimColor, drawerState, drawerContent)
}

@Composable
fun DrawerHub(
    widthSizeClass: WindowWidthSizeClass,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = true,
    scrimColor: Color = DrawerDefaults.scrimColor,
    drawerState:DrawerState = rememberDrawerState(DrawerValue.Closed),
    drawerContent: @Composable ColumnScope.() -> Unit
){
    val sheetWidth = when(widthSizeClass){
        WindowWidthSizeClass.Compact -> 200.dp
        WindowWidthSizeClass.Medium -> 300.dp
        else -> 400.dp
    }
    val ctx = LocalContext.current as ComponentActivity

    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        FlowEventBus.subscribe<Int>(ctx.lifecycle, "Drawer.clickItem"){
            scope.launch {
                drawerState.close()
            }
        }
    }

    if (widthSizeClass == WindowWidthSizeClass.Compact) {
        //Log.d("Router", "use ModalNavigationDrawer")
        ModalNavigationDrawer(
            {
                ModalDrawerSheet(
                    modifier = modifier.width(sheetWidth),
                    drawerShape =  RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                    content = drawerContent
                )
            },
            Modifier,
            drawerState,
            gesturesEnabled,
            scrimColor, content)
    } else {
        //Log.d("Router", "use PermanentNavigationDrawer")
        PermanentNavigationDrawer({
            ModalDrawerSheet(
                modifier = modifier.width(sheetWidth),
                drawerShape =  RectangleShape,
                content = drawerContent
            )
        },Modifier, content)
    }
}

/**
 *
 * */
@Composable
fun <T> ItemsNav(
    items: List<T>,
    onItemClick: (index: Int, item: T) -> Unit,
    index: Int = 0,
    itemModifier: Modifier = Modifier.padding(horizontal = 3.dp) ,
    title: String? = null,
    icons: List<ImageVector>? = null,
    drawItem:  @Composable (T) -> Unit
) {
    //val selectedState = rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val currentIndex = rememberUpdatedState(index)

    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally){
        if(title != null){
            Text(title, Modifier.padding(5.dp, 5.dp, 10.dp, 10.dp), fontWeight = FontWeight.Bold)
            HorizontalDivider()
        }

        Column(Modifier.verticalScroll(rememberScrollState())){
            items.forEachIndexed { index, e ->
                NavigationDrawerItem(
                    label = { drawItem(e) },
                    selected = index == currentIndex.value,
                    onClick = {
                        scope.launch {
                            FlowEventBus.post("Drawer.clickItem", index)
                            //drawerState.close()
                            //selectedState.intValue = index
                            onItemClick(index, e)
                        }
                    },
                    modifier = itemModifier,
                    icon = {icons?.getOrNull(index)?.let{  Icon(it, null) } },
                )
            }
        }

    }
}