package com.github.rwsbillyang.composerouter.example

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.rwsbillyang.composerouter.*
import com.github.rwsbillyang.composerouter.example.ui.theme.ComposeRouterTheme
import com.github.rwsbillyang.composerouter.nav.FlowEventBus
import com.github.rwsbillyang.composerouter.nav.ItemsNav
import com.github.rwsbillyang.composerouter.nav.NavScaffold3
import com.github.rwsbillyang.composerouter.nav.RoutableActivity





class MainActivity : RoutableActivity() { // or use LocalRoutableActivity: routes and route stack history are only valid/visible in Activity instead of globally
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRouterTheme {
                NavScaffold3(R.string.app_name) //with navigation title bar on top
                //router.Screen() //no navigation title bar
            }
        }
    }

    override fun getRoutes() = listOf(
        route("Home",  R.string.home,"/"){ Home() },
        //route("MyScreen1", R.string.screen1){ Screen1(it) },
        route("MyScreen1", R.string.screen1,"/screen1", screen = ScaffoldScreen3(
            { Screen1(it) },
            {
                val router = useRouter()
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { router.navByName("Settings") }) {
                    Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                }
            }
        )),

        //route("MyScreen2", R.string.screen2){ Screen2(it) },
        route("MyScreen2",  R.string.screen2,
            screen = ScaffoldScreen3(
                content = { Screen2(it) },
                drawerContent = {
                    val selectedState = rememberSaveable { mutableIntStateOf(0) }
                    ItemsNav(items = (1..50).map{"item$it"}, onItemClick = {index, it ->
                        selectedState.value = index
                        FlowEventBus.tryPost("onItemClick", Pair(index, it))
                    },selectedState.value, title = "Drawer title") {
                        Text(it, modifier = Modifier.padding(14.dp))
                    }
                }
            )),
        route("MyScreen3",  R.string.screen3){ Screen3(it) },
        route("Settings",  R.string.settings){ Settings(it) },
    )
}

@Composable
fun Home() {
    val router = useRouter()
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {

        //pass props to screen, and modify nav bar title
        Button(onClick = { router.navByPath("/screen1", "any type props", "newTitle of Screen1") }) {
            Text(text = "enter screen1")
        }

        Button(onClick = { router.navByName("MyScreen2") }) {
            Text(text = "enter screen2")
        }

        Button(onClick = { router.navByName("MyScreen3") }) {
            Text(text = "enter screen3")
        }

        Button(onClick = { router.navByName("Settings") }) {
            Text(text = "Settings")
        }
    }
}


//need receive props
@Composable
fun Screen1(call: ScreenCall) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(call.scaffoldPadding), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Hello, ${call.props}, with settings in top-right of nav bar")
    }
}

@Composable
fun Screen2(call: ScreenCall) {
    var itemPair by remember {  mutableStateOf<Pair<Int, String>?>(null) }
    val ctx = LocalContext.current as ComponentActivity
    LaunchedEffect(Unit){
        FlowEventBus.subscribe<Pair<Int, String>>(ctx.lifecycle, "onItemClick"){
            itemPair = it
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(call.scaffoldPadding), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Screen2 with drawer", Modifier.padding(call.scaffoldPadding))
        Text("clicked left drawer: index=${itemPair?.first?:""}, item=${itemPair?.second?:""}")
    }
}

@Composable
fun Screen3(call: ScreenCall) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(call.scaffoldPadding)
            .background(Color.Blue), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Screen3")
    }
}

@Composable
fun Settings(call: ScreenCall) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(call.scaffoldPadding)
            .background(Color.Red), Arrangement.Center, Alignment.CenterHorizontally){
        Text(text = "Settings Screen")
    }
}
