package com.github.rwsbillyang.composerouter.nav


import android.app.Activity
import android.util.Log

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.DrawerDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.rwsbillyang.composerouter.Router
import com.github.rwsbillyang.composerouter.ScaffoldScreen2
import com.github.rwsbillyang.composerouter.ScaffoldScreen3
import com.github.rwsbillyang.composerouter.useRouter


/**
 * Wrapper of Material2 Scaffold with TopAppBar as nav bar
 * */
@Composable
fun NavScaffold2(
    appNameResId: Int,
    modifier: Modifier = Modifier
)
{
    val router = useRouter()

    val screen = router.currentRoute?.screen
    if (screen != null){
        if(screen is ScaffoldScreen2){
            Scaffold(
                modifier,screen.scaffoldState?: rememberScaffoldState(),
                screen.topBar?:{ NavTopAppBar2(appNameResId, screen.topBarActions) },
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
                router.Screen(it)
            }
        }else if(screen is ScaffoldScreen3){
            Log.w(Router.TAG, "You are using ScaffoldScreen3 in NavScaffold2 of material2")
            Scaffold(modifier, topBar =  { NavTopAppBar2(appNameResId) {} })
            {
                router.Screen(it)
            }
        }else{
            if(router.currentRoute!!.useNavScaffold){
                Scaffold(
                    modifier,
                    topBar = { NavTopAppBar2(appNameResId) {} })
                {
                    router.Screen(it)
                }
            }else{//某些特殊情况下，如带drawer的scaffold，自行完全定制，更方便
                router.Screen()
            }
        }
    }
}

@Composable
fun NavTopAppBar2(appNameResId: Int, actions:  @Composable RowScope.() -> Unit){
    val ctx = LocalContext.current
    val router = useRouter()

    val navIcon by remember(router.currentRoute) {
        mutableStateOf(if(router.isLast()) Icons.Filled.Close else Icons.AutoMirrored.Filled.ArrowBack)
    }

    TopAppBar(
        title = {
            val currentRoute = router.currentRoute
            Text(
                (currentRoute?.ctxMap?.get("title") as String?)?: currentRoute?.titleId?.let{stringResource(it)} ?: currentRoute?.name?:stringResource(appNameResId),
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